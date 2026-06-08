package br.com.petsaude.regula_ai_backend.Service;

import br.com.petsaude.regula_ai_backend.Repository.*;
import br.com.petsaude.regula_ai_backend.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImportacaoFilaService {

    private static final DateTimeFormatter[] DATE_TIME_FORMATS = {
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"),
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"),
        DateTimeFormatter.ofPattern("dd/MM/yyyy")
    };

    private final PacienteRepository pacienteRepository;
    private final EncaminhamentoRepository encaminhamentoRepository;
    private final DiagnosticoRepository diagnosticoRepository;
    private final ProcedimentoRepository procedimentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final UnidadeRepository unidadeRepository;
    private final EncaminhamentoProfissionalRepository encaminhamentoProfissionalRepository;
    private final EncaminhamentoEstabelecimentoRepository encaminhamentoEstabelecimentoRepository;
    private final EncaminhamentoProcedimentoRepository encaminhamentoProcedimentoRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final NegativaRepository negativaRepository;

    // ===== PRÉ-CARGA =====

    public LookupContext precarregarLookups() {
        log.info("Pré-carregando dados existentes do banco...");
        LookupContext ctx = new LookupContext();

        pacienteRepository.findAll()
            .forEach(p -> ctx.pacientes.put(p.getCodUsuario(), p));
        usuarioRepository.findAll()
            .forEach(u -> ctx.profissionais.put(u.getNome(), u));
        unidadeRepository.findAll()
            .forEach(u -> ctx.estabelecimentos.put(chaveUnidade(u.getNome(), u.getEndereco()), u));
        procedimentoRepository.findAll()
            .forEach(p -> ctx.procedimentos.put(p.getCodigo(), p));
        diagnosticoRepository.findAll()
            .forEach(d -> ctx.diagnosticos.put(d.getCidCodigo(), d));
        encaminhamentoRepository.findAllCodConsultas()
            .forEach(ctx.codConsultasExistentes::add);

        log.info("Pré-carga concluída — pacientes: {}, profissionais: {}, estabelecimentos: {}, " +
                 "procedimentos: {}, diagnósticos: {}, encaminhamentos existentes: {}",
            ctx.pacientes.size(), ctx.profissionais.size(), ctx.estabelecimentos.size(),
            ctx.procedimentos.size(), ctx.diagnosticos.size(), ctx.codConsultasExistentes.size());

        return ctx;
    }

    // ===== PARSING DO CSV =====

    public List<Map<String, String>> parsearCsv(Path arquivo) throws IOException {
        List<Map<String, String>> linhas = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(arquivo.toFile()), StandardCharsets.UTF_8))) {

            String headerLine = null;
            String line;
            while ((line = reader.readLine()) != null) {
                if (isHeaderLine(line)) {
                    headerLine = line;
                    break;
                }
            }

            if (headerLine == null) {
                throw new IllegalArgumentException("Cabeçalho do CSV não encontrado.");
            }

            char delimiter = detectDelimiter(headerLine);
            Map<String, Integer> headerMap = buildHeaderMap(headerLine, delimiter);
            log.info("CSV detectado — delimitador: '{}', colunas: {}",
                delimiter == '\t' ? "TAB" : String.valueOf(delimiter), headerMap.size());

            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    linhas.add(parseRow(line, delimiter, headerMap));
                }
            }
        }

        return linhas;
    }

    // ===== PROCESSAMENTO POR CHUNK (transacional) =====

    @Transactional
    public ChunkResultado processarChunk(List<Map<String, String>> linhas, LookupContext ctx) {
        int criados = 0, atualizados = 0;
        List<String> erros = new ArrayList<>();

        // 1. Buscar em lote os encaminhamentos existentes neste chunk
        Set<String> codsNoChunk = new LinkedHashSet<>();
        for (Map<String, String> row : linhas) {
            String cod = get(row, "Cod Consulta");
            if (cod != null && !cod.isBlank()) {
                codsNoChunk.add(cod);
            }
        }

        Set<String> codsExistentesNoChunk = new HashSet<>();
        for (String cod : codsNoChunk) {
            if (ctx.codConsultasExistentes.contains(cod)) {
                codsExistentesNoChunk.add(cod);
            }
        }

        Map<String, Encaminhamento> encaminhamentosExistentes = new HashMap<>();
        if (!codsExistentesNoChunk.isEmpty()) {
            encaminhamentoRepository.findAllByCodConsultaIn(codsExistentesNoChunk)
                .forEach(e -> encaminhamentosExistentes.put(e.getCodConsulta(), e));
        }

        // 2. Resolver e salvar pacientes novos em lote
        List<Paciente> novosPacientes = new ArrayList<>();
        for (Map<String, String> row : linhas) {
            String codUsuario = get(row, "Cód Usuário");
            if (codUsuario == null || codUsuario.isBlank()) continue;
            if (!ctx.pacientes.containsKey(codUsuario)) {
                Paciente novo = Paciente.builder().codUsuario(codUsuario).build();
                ctx.pacientes.put(codUsuario, novo);
                novosPacientes.add(novo);
            }
        }
        if (!novosPacientes.isEmpty()) {
            List<Paciente> salvos = pacienteRepository.saveAll(novosPacientes);
            salvos.forEach(p -> ctx.pacientes.put(p.getCodUsuario(), p));
        }

        // 3. Processar linha a linha
        for (Map<String, String> row : linhas) {
            try {
                String codConsulta = get(row, "Cod Consulta");
                String codUsuario = get(row, "Cód Usuário");

                if (codUsuario == null || codUsuario.isBlank()) {
                    erros.add("Linha ignorada: Cód Usuário ausente");
                    continue;
                }
                if (codConsulta == null || codConsulta.isBlank()) {
                    erros.add("Linha ignorada: Cod Consulta ausente");
                    continue;
                }

                Paciente paciente = ctx.pacientes.get(codUsuario);
                LocalDateTime dtUltimaAlteracao = parseDateTime(get(row, "Dt Última Alteração"));

                if (updatePaciente(paciente, row, dtUltimaAlteracao)) {
                    paciente = pacienteRepository.save(paciente);
                    ctx.pacientes.put(paciente.getCodUsuario(), paciente);
                }

                boolean ehNovo = !ctx.codConsultasExistentes.contains(codConsulta);
                Encaminhamento encaminhamento;

                if (ehNovo) {
                    encaminhamento = Encaminhamento.builder()
                        .codConsulta(codConsulta)
                        .paciente(paciente)
                        .build();
                    updateEncaminhamento(encaminhamento, paciente, row, dtUltimaAlteracao, ctx.diagnosticos);
                    encaminhamento = encaminhamentoRepository.save(encaminhamento);
                    ctx.codConsultasExistentes.add(codConsulta);
                    criados++;
                } else {
                    encaminhamento = encaminhamentosExistentes.get(codConsulta);
                    if (encaminhamento != null && shouldUpdate(encaminhamento.getDtUltimaAlteracao(), dtUltimaAlteracao)) {
                        updateEncaminhamento(encaminhamento, paciente, row, dtUltimaAlteracao, ctx.diagnosticos);
                        encaminhamentoRepository.save(encaminhamento);
                        atualizados++;
                    }
                }

                if (encaminhamento != null) {
                    updateRelacionamentos(encaminhamento, row, ctx.profissionais, ctx.estabelecimentos, ctx.procedimentos);
                    updateAgendamento(encaminhamento, row);
                    updateNegativa(encaminhamento, row);
                }

            } catch (Exception ex) {
                if (erros.size() < 50) {
                    erros.add(ex.getMessage());
                }
                log.warn("Falha ao processar linha: {}", ex.getMessage());
            }
        }

        return new ChunkResultado(criados, atualizados, erros);
    }

    // ===== ATUALIZAÇÃO DE ENTIDADES =====

    private boolean updatePaciente(Paciente paciente, Map<String, String> row, LocalDateTime dtAtualizacao) {
        if (!shouldUpdate(paciente.getAtualizadoEm(), dtAtualizacao)) {
            return false;
        }
        paciente.setNumeroProntuario(get(row, "Nº Prontuário"));
        paciente.setSexo(get(row, "Sexo"));
        paciente.setNascimento(parseDate(get(row, "Nascimento")));
        paciente.setIdade(parseInteger(get(row, "Idade")));
        paciente.setBairro(get(row, "Bairro"));
        paciente.setMunicipio(get(row, "Município"));
        paciente.setMicroarea(get(row, "Microarea"));
        paciente.setEquipe(get(row, "Equipe"));
        paciente.setAtualizadoEm(dtAtualizacao != null ? dtAtualizacao : LocalDateTime.now());
        return true;
    }

    private void updateEncaminhamento(Encaminhamento encaminhamento, Paciente paciente,
                                      Map<String, String> row, LocalDateTime dtUltimaAlteracao,
                                      Map<String, Diagnostico> diagnosticoCache) {
        encaminhamento.setPaciente(paciente);
        encaminhamento.setDtCadastro(parseDateTime(get(row, "Dt Cadastro")));
        encaminhamento.setPrioridade(get(row, "Prioridade"));
        encaminhamento.setPosicaoFila(parseInteger(get(row, "Posição")));
        encaminhamento.setRegulacaoFlag(parseBoolean(get(row, "Regulação")));
        encaminhamento.setBuscaAtivaFlag(parseBoolean(get(row, "Busca Ativa")));
        encaminhamento.setTipo(get(row, "Tipo"));
        encaminhamento.setSituacao(get(row, "Situação"));
        encaminhamento.setDtUltimaAlteracao(dtUltimaAlteracao);
        encaminhamento.setMotivoEncaminhamento(get(row, "Motivo do Encaminhamento"));
        encaminhamento.setOrigemSolicitacao(get(row, "Origem da Solicitação"));
        encaminhamento.setProcessoAutomaticoFlag(parseBoolean(get(row, "Processo Automático")));
        encaminhamento.setSolicitacaoLeito(parseBoolean(get(row, "Solicitação Leito")));
        encaminhamento.setDataControle(parseDate(get(row, "Data de Controle")));
        encaminhamento.setPerfil(get(row, "Perfil"));
        encaminhamento.setCidPrincipal(resolveDiagnostico(get(row, "CID- 10"), get(row, "Descrição CID"), diagnosticoCache));
        encaminhamento.setCidPrioritario(resolveDiagnostico(get(row, "CID- 10 Prioritário"), null, diagnosticoCache));
        encaminhamento.setAtualizadoEm(dtUltimaAlteracao != null ? dtUltimaAlteracao : LocalDateTime.now());
    }

    private void updateRelacionamentos(Encaminhamento encaminhamento, Map<String, String> row,
                                       Map<String, Usuario> profissionalCache,
                                       Map<String, Unidade> estabelecimentoCache,
                                       Map<String, Procedimento> procedimentoCache) {
        addProfissional(encaminhamento, get(row, "Profissional Sugerido"), "SUGERIDO", profissionalCache);
        addProfissional(encaminhamento, get(row, "Profissional Solicitante"), "SOLICITANTE", profissionalCache);
        addProfissional(encaminhamento, get(row, "Regulador"), "REGULADOR", profissionalCache);
        addProfissional(encaminhamento, get(row, "Cadastrado por"), "CADASTRADO_POR", profissionalCache);
        addProfissional(encaminhamento, get(row, "Nome Responsável"), "RESPONSAVEL", profissionalCache);
        addProfissional(encaminhamento, get(row, "Resp Negativa"), "RESP_NEGATIVA", profissionalCache);
        addProfissional(encaminhamento, get(row, "Nome Responsável Busca Ativa"), "RESP_BUSCA_ATIVA", profissionalCache);

        addEstabelecimento(encaminhamento, get(row, "Estabelecimento Solicitante"), null, "SOLICITANTE", estabelecimentoCache);
        addEstabelecimento(encaminhamento, get(row, "Estabelecimento Sugerido"), null, "SUGERIDO", estabelecimentoCache);
        addEstabelecimento(encaminhamento, get(row, "Estabelecimento Prestador"),
            get(row, "Endereço do Estabelecimento Prestador"), "PRESTADOR", estabelecimentoCache);
        addEstabelecimento(encaminhamento, get(row, "Estab Responsável Busca Ativa"), null, "RESP_BUSCA_ATIVA", estabelecimentoCache);

        addProcedimento(encaminhamento, get(row, "Procedimento Principal"), "PRINCIPAL", row, procedimentoCache);
        addProcedimento(encaminhamento, get(row, "Procedimento SMS"), "SMS", row, procedimentoCache);
        addProcedimento(encaminhamento, get(row, "Procedimento (s) SUS"), "SUS", row, procedimentoCache);
    }

    private void updateAgendamento(Encaminhamento encaminhamento, Map<String, String> row) {
        LocalDateTime dataConsulta = parseDateTime(get(row, "Data Consulta"));
        LocalDateTime dataReservada = parseDateTime(get(row, "Data Reservada"));
        LocalDateTime limiteConfirmacao = parseDateTime(get(row, "Limite Confirmação Reserva"));
        LocalDateTime dtRealizacao = parseDateTime(get(row, "Dt Realização Agendamento"));

        if (dataConsulta == null && dataReservada == null && limiteConfirmacao == null && dtRealizacao == null) {
            return;
        }

        Agendamento agendamento = encaminhamento.getAgendamento();
        if (agendamento == null) {
            agendamento = Agendamento.builder().encaminhamento(encaminhamento).build();
        }
        agendamento.setDataConsulta(dataConsulta);
        agendamento.setDataReservada(dataReservada);
        agendamento.setLimiteConfirmacaoReserva(limiteConfirmacao);
        agendamento.setDtRealizacaoAgendamento(dtRealizacao);
        agendamentoRepository.save(agendamento);
        encaminhamento.setAgendamento(agendamento);
    }

    private void updateNegativa(Encaminhamento encaminhamento, Map<String, String> row) {
        String motivoNegativa = get(row, "Motivo da Negativa");
        String justifNegativa = get(row, "Justif da Negativa");
        String respNegativa = get(row, "Resp Negativa");
        String justifCancelamento = get(row, "Justif Cancelamento");
        LocalDate dataNegativa = parseDate(get(row, "Data da Negativa"));

        if (isBlankAll(motivoNegativa, justifNegativa, respNegativa, justifCancelamento) && dataNegativa == null) {
            return;
        }

        Negativa negativa = encaminhamento.getNegativa();
        if (negativa == null) {
            negativa = Negativa.builder().encaminhamento(encaminhamento).build();
        }
        negativa.setMotivoNegativa(motivoNegativa);
        negativa.setJustificativaNegativa(justifNegativa);
        negativa.setResponsavelNegativa(respNegativa);
        negativa.setJustificativaCancelamento(justifCancelamento);
        negativa.setDataNegativa(dataNegativa);
        negativaRepository.save(negativa);
        encaminhamento.setNegativa(negativa);
    }

    private void addProfissional(Encaminhamento encaminhamento, String nome, String papel,
                                 Map<String, Usuario> profissionalCache) {
        if (nome == null || nome.isBlank()) return;

        Usuario profissional = profissionalCache.computeIfAbsent(nome, key ->
            usuarioRepository.save(Usuario.builder().nome(key).build()));

        boolean existe = encaminhamento.getProfissionais().stream()
            .anyMatch(item -> item.getProfissional().getId().equals(profissional.getId())
                && papel.equals(item.getPapel()));
        if (existe) return;

        EncaminhamentoProfissional rel = EncaminhamentoProfissional.builder()
            .encaminhamento(encaminhamento)
            .profissional(profissional)
            .papel(papel)
            .build();
        encaminhamento.getProfissionais().add(rel);
        encaminhamentoProfissionalRepository.save(rel);
    }

    private void addEstabelecimento(Encaminhamento encaminhamento, String nome, String endereco,
                                    String papel, Map<String, Unidade> estabelecimentoCache) {
        if (nome == null || nome.isBlank()) return;

        String key = chaveUnidade(nome, endereco);
        Unidade estabelecimento = estabelecimentoCache.computeIfAbsent(key, k ->
            unidadeRepository.save(Unidade.builder().nome(nome).endereco(endereco).build()));

        boolean existe = encaminhamento.getEstabelecimentos().stream()
            .anyMatch(item -> item.getEstabelecimento().getId().equals(estabelecimento.getId())
                && papel.equals(item.getPapel()));
        if (existe) return;

        EncaminhamentoEstabelecimento rel = EncaminhamentoEstabelecimento.builder()
            .encaminhamento(encaminhamento)
            .estabelecimento(estabelecimento)
            .papel(papel)
            .build();
        encaminhamento.getEstabelecimentos().add(rel);
        encaminhamentoEstabelecimentoRepository.save(rel);
    }

    private void addProcedimento(Encaminhamento encaminhamento, String raw, String tipo,
                                 Map<String, String> row, Map<String, Procedimento> procedimentoCache) {
        if (raw == null || raw.isBlank()) return;

        ProcedimentoParse parsed = parseProcedimento(raw);
        if (parsed.codigo == null) return;

        Procedimento procedimento = procedimentoCache.computeIfAbsent(parsed.codigo, key ->
            procedimentoRepository.save(Procedimento.builder()
                .codigo(parsed.codigo)
                .descricao(parsed.descricao)
                .build()));

        Integer quantidade = parseInteger(get(row, "Qtde Solicitada"));

        boolean existe = encaminhamento.getProcedimentos().stream()
            .anyMatch(item -> item.getProcedimento().getId().equals(procedimento.getId())
                && tipo.equals(item.getTipo()));
        if (existe) return;

        EncaminhamentoProcedimento rel = EncaminhamentoProcedimento.builder()
            .encaminhamento(encaminhamento)
            .procedimento(procedimento)
            .tipo(tipo)
            .quantidade(quantidade)
            .build();
        encaminhamento.getProcedimentos().add(rel);
        encaminhamentoProcedimentoRepository.save(rel);
    }

    private Diagnostico resolveDiagnostico(String cidCodigo, String descricao,
                                           Map<String, Diagnostico> diagnosticoCache) {
        if (cidCodigo == null || cidCodigo.isBlank()) return null;
        return diagnosticoCache.computeIfAbsent(cidCodigo, key ->
            diagnosticoRepository.save(Diagnostico.builder()
                .cidCodigo(key)
                .descricao(descricao)
                .build()));
    }

    // ===== HELPERS DE PARSING =====

    private boolean shouldUpdate(LocalDateTime atual, LocalDateTime nova) {
        if (nova == null) return atual == null;
        return atual == null || nova.isAfter(atual);
    }

    private Map<String, Integer> buildHeaderMap(String header, char delimiter) {
        String[] parts = splitLine(header, delimiter);
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < parts.length; i++) {
            map.put(normalizeHeader(parts[i]), i);
        }
        return map;
    }

    private Map<String, String> parseRow(String line, char delimiter, Map<String, Integer> headerMap) {
        String[] parts = splitLine(line, delimiter);
        Map<String, String> row = new HashMap<>();
        for (Map.Entry<String, Integer> entry : headerMap.entrySet()) {
            int idx = entry.getValue();
            if (idx >= 0 && idx < parts.length) {
                row.put(entry.getKey(), clean(parts[idx]));
            }
        }
        return row;
    }

    private String get(Map<String, String> row, String key) {
        return row.get(normalizeHeader(key));
    }

    private String[] splitLine(String line, char delimiter) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        current.append('"');
                        i++;
                    } else {
                        inQuotes = false;
                    }
                } else {
                    current.append(c);
                }
            } else {
                if (c == '"') {
                    inQuotes = true;
                } else if (c == delimiter) {
                    fields.add(current.toString());
                    current.setLength(0);
                } else {
                    current.append(c);
                }
            }
        }
        fields.add(current.toString());
        return fields.toArray(new String[0]);
    }

    private String clean(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        if (trimmed.startsWith("\"") && trimmed.endsWith("\"") && trimmed.length() > 1) {
            trimmed = trimmed.substring(1, trimmed.length() - 1).trim();
        }
        return trimmed;
    }

    private boolean isHeaderLine(String line) {
        String normalized = normalizeHeader(line);
        return normalized.contains(normalizeHeader("Cód Usuário"))
            && normalized.contains(normalizeHeader("Cod Consulta"));
    }

    private char detectDelimiter(String headerLine) {
        long tabs = headerLine.chars().filter(ch -> ch == '\t').count();
        long semis = headerLine.chars().filter(ch -> ch == ';').count();
        long commas = headerLine.chars().filter(ch -> ch == ',').count();
        if (tabs >= semis && tabs >= commas) return '\t';
        if (semis >= commas) return ';';
        return ',';
    }

    private String normalizeHeader(String value) {
        if (value == null) return "";
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD)
            .replaceAll("\\p{M}", "");
        normalized = normalized.replace("﻿", "");
        normalized = normalized.replaceAll("\\s+", " ").trim().toLowerCase(Locale.ROOT);
        return normalized;
    }

    private LocalDateTime parseDateTime(String raw) {
        if (raw == null || raw.isBlank()) return null;
        for (DateTimeFormatter fmt : DATE_TIME_FORMATS) {
            try {
                return LocalDateTime.parse(raw, fmt);
            } catch (DateTimeParseException ignored) {}
        }
        try {
            return LocalDate.parse(raw, DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay();
        } catch (DateTimeParseException ignored) {
            return null;
        }
    }

    private LocalDate parseDate(String raw) {
        if (raw == null || raw.isBlank()) return null;
        for (DateTimeFormatter fmt : DATE_TIME_FORMATS) {
            try {
                return LocalDate.parse(raw, fmt);
            } catch (DateTimeParseException ignored) {}
        }
        return null;
    }

    private Integer parseInteger(String raw) {
        if (raw == null || raw.isBlank()) return null;
        try {
            return Integer.parseInt(raw.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private Boolean parseBoolean(String raw) {
        if (raw == null || raw.isBlank()) return null;
        String normalized = normalizeHeader(raw);
        if (normalized.equals("sim") || normalized.equals("s") || normalized.equals("true")) return true;
        if (normalized.equals("nao") || normalized.equals("n") || normalized.equals("false")) return false;
        return null;
    }

    private ProcedimentoParse parseProcedimento(String raw) {
        String value = raw.trim();
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("\\b\\d{6,12}\\b").matcher(value);
        String codigo = matcher.find() ? matcher.group() : null;
        String descricao = codigo != null ? value.replace(codigo, "").replace("-", "").trim() : value;
        return new ProcedimentoParse(codigo, descricao.isBlank() ? value : descricao);
    }

    private static String chaveUnidade(String nome, String endereco) {
        return nome + "|" + (endereco == null ? "" : endereco);
    }

    private static boolean isBlankAll(String... values) {
        for (String v : values) {
            if (v != null && !v.isBlank()) return false;
        }
        return true;
    }

    // ===== INNER TYPES =====

    public static class LookupContext {
        public final Map<String, Paciente> pacientes = new HashMap<>();
        public final Map<String, Usuario> profissionais = new HashMap<>();
        public final Map<String, Unidade> estabelecimentos = new HashMap<>();
        public final Map<String, Procedimento> procedimentos = new HashMap<>();
        public final Map<String, Diagnostico> diagnosticos = new HashMap<>();
        public final Set<String> codConsultasExistentes = new HashSet<>();
    }

    public record ChunkResultado(int criados, int atualizados, List<String> erros) {}

    private static class ProcedimentoParse {
        final String codigo;
        final String descricao;
        ProcedimentoParse(String codigo, String descricao) {
            this.codigo = codigo;
            this.descricao = descricao;
        }
    }
}
