package br.com.petsaude.regula_ai_backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO de resposta completo para uma Solicitação.
 * Contém todos os dados da entidade incluindo relacionamentos e itens com snapshot.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolicitacaoResponseDTO {

    private UUID id;

    // ==========================================
    // Campos FHIR
    // ==========================================

    /**
     * FHIR intent (ex: order)
     */
    private String intent;

    /**
     * FHIR status (ex: draft, active, on-hold)
     */
    private String statusFhir;

    /**
     * FHIR priority (ex: routine, urgent, asap, stat)
     */
    private String priority;

    /**
     * Data/hora da criação do pedido
     */
    private LocalDateTime authoredOn;

    // ==========================================
    // Campos Flattened - Categoria
    // ==========================================

    private String categorySystem;
    private String categoryCode;
    private String categoryDisplay;

    // ==========================================
    // Campos Flattened - Diagnóstico (CID)
    // ==========================================

    private String reasonSystem;
    private String reasonCode;
    private String reasonDisplay;

    // ==========================================
    // Campos de Negócio
    // ==========================================

    private String note;
    private Boolean queueFlag;
    private String statusRegulacao;

    // ==========================================
    // Relacionamentos (DTOs resumidos)
    // ==========================================

    private PacienteResumoDTO paciente;
    private UsuarioResumoDTO medicoSolicitante;
    private UnidadeResumoDTO unidadeExecutante;
    private UsuarioResumoDTO medicoRegulador;

    // ==========================================
    // Itens da solicitação (com dados do snapshot)
    // ==========================================

    private List<ItemSolicitacaoResponseDTO> itens;

    // ==========================================
    // Auditoria
    // ==========================================

    private LocalDateTime criadoEm;
}
