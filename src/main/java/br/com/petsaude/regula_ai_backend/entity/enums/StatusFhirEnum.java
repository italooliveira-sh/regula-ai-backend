package br.com.petsaude.regula_ai_backend.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Status FHIR para ServiceRequest conforme especificação HL7 FHIR R4.
 * @see <a href="https://hl7.org/fhir/R4/valueset-request-status.html">FHIR Request Status</a>
 */
@Getter
@RequiredArgsConstructor
public enum StatusFhirEnum {

    DRAFT("draft", "Rascunho"),
    ACTIVE("active", "Ativo"),
    ON_HOLD("on-hold", "Em Espera"),
    REVOKED("revoked", "Revogado"),
    COMPLETED("completed", "Completo"),
    ENTERED_IN_ERROR("entered-in-error", "Erro de Entrada"),
    UNKNOWN("unknown", "Desconhecido");

    private final String fhirCode;
    private final String descricao;
}
