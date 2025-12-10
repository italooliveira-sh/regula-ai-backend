package br.com.petsaude.regula_ai_backend.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * DTO de entrada para criação de uma nova solicitação de serviço.
 * Mapeia os dados necessários para criar um ServiceRequest FHIR.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolicitacaoCreateDTO {

    // ==========================================
    // Identificadores obrigatórios
    // ==========================================

    @NotNull(message = "O ID do paciente é obrigatório")
    private UUID pacienteId;

    @NotNull(message = "O ID do médico solicitante é obrigatório")
    private UUID medicoSolicitanteId;

    @NotNull(message = "O ID da unidade executante é obrigatório")
    private UUID unidadeExecutanteId;

    // ==========================================
    // Itens da solicitação
    // ==========================================

    @NotEmpty(message = "A solicitação deve conter pelo menos um item")
    @Valid
    private List<ItemSolicitacaoDTO> itens;

    // ==========================================
    // Campos FHIR
    // ==========================================

    /**
     * Prioridade FHIR: routine, urgent, asap, stat.
     * Default: routine
     */
    @Pattern(regexp = "^(routine|urgent|asap|stat)$", 
             message = "Prioridade deve ser: routine, urgent, asap ou stat")
    private String priority;

    /**
     * Notas/observações clínicas (justificativa).
     */
    private String note;

    // ==========================================
    // Campos Flattened - Categoria
    // ==========================================

    /**
     * Sistema de codificação da categoria.
     * Ex: http://snomed.info/sct
     */
    private String categorySystem;

    /**
     * Código da categoria.
     */
    private String categoryCode;

    /**
     * Descrição da categoria.
     */
    private String categoryDisplay;

    // ==========================================
    // Campos Flattened - Diagnóstico (CID)
    // ==========================================

    /**
     * Sistema de codificação do diagnóstico.
     * Ex: http://hl7.org/fhir/sid/icd-10
     */
    private String reasonSystem;

    /**
     * Código CID-10 do diagnóstico principal.
     */
    private String reasonCode;

    /**
     * Descrição do diagnóstico.
     */
    private String reasonDisplay;

    // ==========================================
    // Campos de negócio
    // ==========================================

    /**
     * Flag para encaminhar para fila de atendimento.
     * Default: false
     */
    private Boolean queueFlag;
}
