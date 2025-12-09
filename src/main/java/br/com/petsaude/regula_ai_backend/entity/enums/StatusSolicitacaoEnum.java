package br.com.petsaude.regula_ai_backend.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Status interno do processo de regulação.
 * Este enum representa o status de negócio, separado do status FHIR.
 */
@Getter
@RequiredArgsConstructor
public enum StatusSolicitacaoEnum {

    PENDENTE("Pendente de Regulação"),
    EM_ANALISE("Em Análise"),
    REGULADA("Regulada/Aprovada"),
    DEVOLVIDA("Devolvida para Correção"),
    NEGADA("Negada"),
    CANCELADA("Cancelada"),
    EXECUTADA("Executada/Concluída");

    private final String descricao;
}
