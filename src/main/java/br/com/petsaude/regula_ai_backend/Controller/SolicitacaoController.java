package br.com.petsaude.regula_ai_backend.Controller;

import br.com.petsaude.regula_ai_backend.DTO.SolicitacaoCreateDTO;
import br.com.petsaude.regula_ai_backend.DTO.SolicitacaoResponseDTO;
import br.com.petsaude.regula_ai_backend.Service.SolicitacaoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@Slf4j
@RestController
@RequestMapping("/api/solicitacoes")
@RequiredArgsConstructor
public class SolicitacaoController {

    private final SolicitacaoService solicitacaoService;

    /**
     * Cria uma nova solicitação de serviço.
     * 
     * POST /api/solicitacoes
     * 
     * @param dto Dados para criação da solicitação
     * @return Solicitação criada com status 201 (Created)
     */
    @PostMapping
    public ResponseEntity<SolicitacaoResponseDTO> criarSolicitacao(
            @Valid @RequestBody SolicitacaoCreateDTO dto) {
        log.info("POST /api/solicitacoes - Criando nova solicitação");
        SolicitacaoResponseDTO response = solicitacaoService.criarSolicitacao(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Busca uma solicitação por ID.
     * 
     * GET /api/solicitacoes/{id}
     * 
     * @param id ID da solicitação
     * @return Dados da solicitação
     */
    @GetMapping("/{id}")
    public ResponseEntity<SolicitacaoResponseDTO> buscarPorId(@PathVariable UUID id) {
        log.info("GET /api/solicitacoes/{} - Buscando solicitação", id);
        SolicitacaoResponseDTO response = solicitacaoService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista todas as solicitações com paginação.
     * 
     * GET /api/solicitacoes
     * 
     * @param pageable Configuração de paginação (default: page=0, size=20, sort=authoredOn,desc)
     * @return Página de solicitações
     */
    @GetMapping
    public ResponseEntity<Page<SolicitacaoResponseDTO>> listarTodas(
            @PageableDefault(size = 20, sort = "authoredOn", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("GET /api/solicitacoes - Listando todas (página: {})", pageable.getPageNumber());
        Page<SolicitacaoResponseDTO> response = solicitacaoService.listarTodas(pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista solicitações por paciente.
     * 
     * GET /api/solicitacoes/paciente/{pacienteId}
     * 
     * @param pacienteId ID do paciente
     * @param pageable Configuração de paginação
     * @return Página de solicitações do paciente
     */
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<Page<SolicitacaoResponseDTO>> listarPorPaciente(
            @PathVariable UUID pacienteId,
            @PageableDefault(size = 20, sort = "authoredOn", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("GET /api/solicitacoes/paciente/{} - Listando por paciente", pacienteId);
        Page<SolicitacaoResponseDTO> response = solicitacaoService.listarPorPaciente(pacienteId, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista solicitações por médico solicitante.
     * 
     * GET /api/solicitacoes/medico/{medicoId}
     * 
     * @param medicoId ID do médico
     * @param pageable Configuração de paginação
     * @return Página de solicitações do médico
     */
    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<Page<SolicitacaoResponseDTO>> listarPorMedico(
            @PathVariable UUID medicoId,
            @PageableDefault(size = 20, sort = "authoredOn", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("GET /api/solicitacoes/medico/{} - Listando por médico", medicoId);
        Page<SolicitacaoResponseDTO> response = solicitacaoService.listarPorMedicoSolicitante(medicoId, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista solicitações pendentes de regulação.
     * 
     * GET /api/solicitacoes/pendentes
     * 
     * @return Lista de solicitações pendentes ordenadas por data
     */
    @GetMapping("/pendentes")
    public ResponseEntity<List<SolicitacaoResponseDTO>> listarPendentes() {
        log.info("GET /api/solicitacoes/pendentes - Listando pendentes");
        List<SolicitacaoResponseDTO> response = solicitacaoService.listarPendentes();
        return ResponseEntity.ok(response);
    }

    /**
     * Lista solicitações por status de regulação.
     * 
     * GET /api/solicitacoes/status/{status}
     * 
     * @param status Status de regulação (PENDENTE, EM_ANALISE, REGULADA, DEVOLVIDA, NEGADA, CANCELADA, EXECUTADA)
     * @param pageable Configuração de paginação
     * @return Página de solicitações
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<SolicitacaoResponseDTO>> listarPorStatus(
            @PathVariable String status,
            @PageableDefault(size = 20, sort = "authoredOn", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("GET /api/solicitacoes/status/{} - Listando por status", status);
        Page<SolicitacaoResponseDTO> response = solicitacaoService.listarPorStatusRegulacao(status, pageable);
        return ResponseEntity.ok(response);
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
        log.warn("Entidade não encontrada: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Recurso não encontrado",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Argumento inválido: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Requisição inválida",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    public record ErrorResponse(
            int status,
            String error,
            String message
    ) {}
}
