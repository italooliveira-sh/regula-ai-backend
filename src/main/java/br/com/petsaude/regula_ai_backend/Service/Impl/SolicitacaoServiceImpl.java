package br.com.petsaude.regula_ai_backend.Service.Impl;

import br.com.petsaude.regula_ai_backend.DTO.*;
import br.com.petsaude.regula_ai_backend.Repository.*;
import br.com.petsaude.regula_ai_backend.Service.SolicitacaoService;
import br.com.petsaude.regula_ai_backend.entity.*;
import br.com.petsaude.regula_ai_backend.entity.enums.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
public class SolicitacaoServiceImpl implements SolicitacaoService {

    private final SolicitacaoRepository solicitacaoRepository;
    private final PacienteRepository pacienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final UnidadeRepository unidadeRepository;
    private final ProcedimentoRepository procedimentoRepository;

    @Override
    @Transactional
    public SolicitacaoResponseDTO criarSolicitacao(SolicitacaoCreateDTO dto) {
        log.info("Iniciando criação de solicitação para paciente: {}", dto.getPacienteId());

        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Paciente não encontrado com ID: " + dto.getPacienteId()));

        Usuario medicoSolicitante = usuarioRepository.findById(dto.getMedicoSolicitanteId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Médico solicitante não encontrado com ID: " + dto.getMedicoSolicitanteId()));

        Unidade unidadeExecutante = unidadeRepository.findById(dto.getUnidadeExecutanteId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Unidade executante não encontrada com ID: " + dto.getUnidadeExecutanteId()));

        Solicitacao solicitacao = Solicitacao.builder()
                .paciente(paciente)
                .medicoSolicitante(medicoSolicitante)
                .unidadeExecutante(unidadeExecutante)
                .intent(IntentEnum.ORDER)
                .statusFhir(StatusFhirEnum.ACTIVE)
                .authoredOn(LocalDateTime.now())
                .priority(parsePriority(dto.getPriority()))
                .categorySystem(dto.getCategorySystem())
                .categoryCode(dto.getCategoryCode())
                .categoryDisplay(dto.getCategoryDisplay())
                .reasonSystem(dto.getReasonSystem())
                .reasonCode(dto.getReasonCode())
                .reasonDisplay(dto.getReasonDisplay())
                .note(dto.getNote())
                .queueFlag(dto.getQueueFlag() != null ? dto.getQueueFlag() : false)
                .statusRegulacao(StatusSolicitacaoEnum.PENDENTE)
                .build();

        for (ItemSolicitacaoDTO itemDto : dto.getItens()) {
            Procedimento procedimento = procedimentoRepository.findById(itemDto.getProcedimentoId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Procedimento não encontrado com ID: " + itemDto.getProcedimentoId()));

            if (!procedimento.getAtivo()) {
                throw new IllegalArgumentException(
                        "Procedimento inativo não pode ser solicitado: " + procedimento.getCodigo());
            }

            ItemSolicitacao item = ItemSolicitacao.builder()
                    .code(procedimento.getCodigo())
                    .display(procedimento.getNome())
                    .codeSystem(procedimento.getSistema())
                    .quantity(itemDto.getQuantidade())
                    .build();

            solicitacao.addItem(item);

            log.debug("Item adicionado com snapshot: code={}, display={}, system={}",
                    item.getCode(), item.getDisplay(), item.getCodeSystem());
        }

        Solicitacao solicitacaoSalva = solicitacaoRepository.save(solicitacao);
        log.info("Solicitação criada com sucesso. ID: {}", solicitacaoSalva.getId());

        return toResponseDTO(solicitacaoSalva);
    }

    @Override
    @Transactional(readOnly = true)
    public SolicitacaoResponseDTO buscarPorId(UUID id) {
        log.info("Buscando solicitação por ID: {}", id);

        Solicitacao solicitacao = solicitacaoRepository.findByIdWithRelationships(id);

        if (solicitacao == null) {
            throw new EntityNotFoundException("Solicitação não encontrada com ID: " + id);
        }

        return toResponseDTO(solicitacao);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SolicitacaoResponseDTO> listarTodas(Pageable pageable) {
        log.info("Listando todas as solicitações - página: {}", pageable.getPageNumber());
        return solicitacaoRepository.findAll(pageable).map(this::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SolicitacaoResponseDTO> listarPorPaciente(UUID pacienteId, Pageable pageable) {
        log.info("Listando solicitações do paciente: {}", pacienteId);
        return solicitacaoRepository.findByPacienteId(pacienteId, pageable).map(this::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SolicitacaoResponseDTO> listarPorMedicoSolicitante(UUID medicoId, Pageable pageable) {
        log.info("Listando solicitações do médico: {}", medicoId);
        return solicitacaoRepository.findByMedicoSolicitanteId(medicoId, pageable).map(this::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitacaoResponseDTO> listarPendentes() {
        log.info("Listando solicitações pendentes de regulação");
        return solicitacaoRepository.findPendentesOrdenadoPorData(StatusSolicitacaoEnum.PENDENTE)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SolicitacaoResponseDTO> listarPorStatusRegulacao(String status, Pageable pageable) {
        log.info("Listando solicitações por status: {}", status);
        StatusSolicitacaoEnum statusEnum = StatusSolicitacaoEnum.valueOf(status.toUpperCase());
        return solicitacaoRepository.findByStatusRegulacao(statusEnum, pageable).map(this::toResponseDTO);
    }

    private SolicitacaoResponseDTO toResponseDTO(Solicitacao entity) {
        return SolicitacaoResponseDTO.builder()
                .id(entity.getId())
                .intent(entity.getIntent().getFhirCode())
                .statusFhir(entity.getStatusFhir().getFhirCode())
                .priority(entity.getPriority().getFhirCode())
                .authoredOn(entity.getAuthoredOn())
                .categorySystem(entity.getCategorySystem())
                .categoryCode(entity.getCategoryCode())
                .categoryDisplay(entity.getCategoryDisplay())
                .reasonSystem(entity.getReasonSystem())
                .reasonCode(entity.getReasonCode())
                .reasonDisplay(entity.getReasonDisplay())
                .note(entity.getNote())
                .queueFlag(entity.getQueueFlag())
                .statusRegulacao(entity.getStatusRegulacao().name())
                .paciente(toPacienteResumoDTO(entity.getPaciente()))
                .medicoSolicitante(toUsuarioResumoDTO(entity.getMedicoSolicitante()))
                .unidadeExecutante(toUnidadeResumoDTO(entity.getUnidadeExecutante()))
                .medicoRegulador(entity.getMedicoRegulador() != null ?
                        toUsuarioResumoDTO(entity.getMedicoRegulador()) : null)
                .itens(entity.getItens().stream()
                        .map(this::toItemResponseDTO)
                        .collect(Collectors.toList()))
                .criadoEm(entity.getCriadoEm())
                .build();
    }

    private ItemSolicitacaoResponseDTO toItemResponseDTO(ItemSolicitacao item) {
        return ItemSolicitacaoResponseDTO.builder()
                .id(item.getId())
                .code(item.getCode())
                .display(item.getDisplay())
                .codeSystem(item.getCodeSystem())
                .quantity(item.getQuantity())
                .build();
    }

    private PacienteResumoDTO toPacienteResumoDTO(Paciente paciente) {
        if (paciente == null) return null;
        return PacienteResumoDTO.builder()
                .id(paciente.getId())
                .nome(paciente.getNome())
                .cpf(paciente.getCpf())
                .cartaoSus(paciente.getCartaoSus())
                .dataNascimento(paciente.getDataNascimento())
                .build();
    }

    private UsuarioResumoDTO toUsuarioResumoDTO(Usuario usuario) {
        if (usuario == null) return null;
        return UsuarioResumoDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .crm(usuario.getCrm())
                .tipo(usuario.getTipo() != null ? usuario.getTipo().name() : null)
                .build();
    }

    private UnidadeResumoDTO toUnidadeResumoDTO(Unidade unidade) {
        if (unidade == null) return null;
        return UnidadeResumoDTO.builder()
                .id(unidade.getId())
                .nome(unidade.getNome())
                .cnes(unidade.getCnes())
                .tipoUnidade(unidade.getTipoUnidade())
                .build();
    }

    /**
     * Converte string de prioridade para enum, com default ROUTINE.
     */
    private PriorityEnum parsePriority(String priority) {
        if (priority == null || priority.isBlank()) {
            return PriorityEnum.ROUTINE;
        }
        return switch (priority.toLowerCase()) {
            case "urgent" -> PriorityEnum.URGENT;
            case "asap" -> PriorityEnum.ASAP;
            case "stat" -> PriorityEnum.STAT;
            default -> PriorityEnum.ROUTINE;
        };
    }
}
