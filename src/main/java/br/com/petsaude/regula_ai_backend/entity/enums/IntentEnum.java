package br.com.petsaude.regula_ai_backend.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Intent FHIR para ServiceRequest conforme especificação HL7 FHIR R4.
 * @see <a href="https://hl7.org/fhir/R4/valueset-request-intent.html">FHIR Request Intent</a>
 */
@Getter
@RequiredArgsConstructor
public enum IntentEnum {

    PROPOSAL("proposal", "Proposta"),
    PLAN("plan", "Plano"),
    DIRECTIVE("directive", "Diretiva"),
    ORDER("order", "Pedido"),
    ORIGINAL_ORDER("original-order", "Pedido Original"),
    REFLEX_ORDER("reflex-order", "Pedido Reflexo"),
    FILLER_ORDER("filler-order", "Pedido de Preenchimento"),
    INSTANCE_ORDER("instance-order", "Instância de Pedido"),
    OPTION("option", "Opção");

    private final String fhirCode;
    private final String descricao;
}
