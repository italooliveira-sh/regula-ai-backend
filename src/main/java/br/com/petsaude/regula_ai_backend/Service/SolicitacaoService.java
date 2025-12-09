package br.com.petsaude.regula_ai_backend.Service;

import br.com.petsaude.regula_ai_backend.DTO.SolicitacaoCreateDTO;
import br.com.petsaude.regula_ai_backend.DTO.SolicitacaoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Interface de serviço para operações de Solicitação.
 */
public interface SolicitacaoService {

    /**
     * Cria uma nova solicitação aplicando o padrão Snapshot nos itens.
     *
     * @param dto Dados para criação da solicitação
     * @return DTO de resposta com a solicitação criada
     */
    SolicitacaoResponseDTO criarSolicitacao(SolicitacaoCreateDTO dto);

    /**
     * Busca uma solicitação por ID.
     *
     * @param id ID da solicitação
     * @return DTO de resposta com os dados da solicitação
     */
    SolicitacaoResponseDTO buscarPorId(UUID id);

    /**
     * Lista todas as solicitações com paginação.
     *
     * @param pageable Configuração de paginação
     * @return Página de solicitações
     */
    Page<SolicitacaoResponseDTO> listarTodas(Pageable pageable);

    /**
     * Lista solicitações por paciente.
     *
     * @param pacienteId ID do paciente
     * @param pageable Configuração de paginação
     * @return Página de solicitações do paciente
     */
    Page<SolicitacaoResponseDTO> listarPorPaciente(UUID pacienteId, Pageable pageable);

    /**
     * Lista solicitações por médico solicitante.
     *
     * @param medicoId ID do médico
     * @param pageable Configuração de paginação
     * @return Página de solicitações do médico
     */
    Page<SolicitacaoResponseDTO> listarPorMedicoSolicitante(UUID medicoId, Pageable pageable);

    /**
     * Lista solicitações pendentes de regulação.
     *
     * @return Lista de solicitações pendentes ordenadas por data
     */
    List<SolicitacaoResponseDTO> listarPendentes();

    /**
     * Lista solicitações por status de regulação com paginação.
     *
     * @param status Status de regulação
     * @param pageable Configuração de paginação
     * @return Página de solicitações
     */
    Page<SolicitacaoResponseDTO> listarPorStatusRegulacao(String status, Pageable pageable);
}
