package br.com.petsaude.regula_ai_backend.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Prioridade FHIR para ServiceRequest conforme especificação HL7 FHIR R4.
 * @see <a href="https://hl7.org/fhir/R4/valueset-request-priority.html">FHIR Request Priority</a>
 */
@Getter
@RequiredArgsConstructor
public enum PriorityEnum {

    ROUTINE("routine", "Rotina"),
    URGENT("urgent", "Urgente"),
    ASAP("asap", "O mais rápido possível"),
    STAT("stat", "Imediato/Emergência");

    private final String fhirCode;
    private final String descricao;
}
