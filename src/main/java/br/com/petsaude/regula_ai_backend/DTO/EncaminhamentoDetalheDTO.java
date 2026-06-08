package br.com.petsaude.regula_ai_backend.DTO;

import br.com.petsaude.regula_ai_backend.entity.Encaminhamento;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record EncaminhamentoDetalheDTO(
        UUID id,
        String codConsulta,
        PacienteDTO paciente,
        String prioridade,
        String situacao,
        Integer posicaoFila,
        String tipo,
        LocalDateTime dtCadastro,
        LocalDateTime dtUltimaAlteracao,
        LocalDate dataControle,
        Boolean regulacaoFlag,
        Boolean buscaAtivaFlag,
        String motivoEncaminhamento,
        String origemSolicitacao,
        Boolean processoAutomaticoFlag,
        Boolean solicitacaoLeito,
        String perfil,
        DiagnosticoDTO cidPrincipal,
        DiagnosticoDTO cidPrioritario,
        List<EncaminhamentoEstabelecimentoDTO> estabelecimentos,
        List<EncaminhamentoProfissionalDTO> profissionais,
        List<EncaminhamentoProcedimentoDTO> procedimentos,
        AgendamentoDTO agendamento,
        NegativaDTO negativa,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {
    public static EncaminhamentoDetalheDTO from(Encaminhamento e) {
        return new EncaminhamentoDetalheDTO(
                e.getId(),
                e.getCodConsulta(),
                e.getPaciente() != null ? PacienteDTO.from(e.getPaciente()) : null,
                e.getPrioridade(),
                e.getSituacao(),
                e.getPosicaoFila(),
                e.getTipo(),
                e.getDtCadastro(),
                e.getDtUltimaAlteracao(),
                e.getDataControle(),
                e.getRegulacaoFlag(),
                e.getBuscaAtivaFlag(),
                e.getMotivoEncaminhamento(),
                e.getOrigemSolicitacao(),
                e.getProcessoAutomaticoFlag(),
                e.getSolicitacaoLeito(),
                e.getPerfil(),
                e.getCidPrincipal() != null ? DiagnosticoDTO.from(e.getCidPrincipal()) : null,
                e.getCidPrioritario() != null ? DiagnosticoDTO.from(e.getCidPrioritario()) : null,
                e.getEstabelecimentos().stream().map(EncaminhamentoEstabelecimentoDTO::from).toList(),
                e.getProfissionais().stream().map(EncaminhamentoProfissionalDTO::from).toList(),
                e.getProcedimentos().stream().map(EncaminhamentoProcedimentoDTO::from).toList(),
                e.getAgendamento() != null ? AgendamentoDTO.from(e.getAgendamento()) : null,
                e.getNegativa() != null ? NegativaDTO.from(e.getNegativa()) : null,
                e.getCriadoEm(),
                e.getAtualizadoEm()
        );
    }
}
