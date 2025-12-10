package br.com.petsaude.regula_ai_backend.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Tipo de usuário no sistema de regulação.
 */
@Getter
@RequiredArgsConstructor
public enum TipoUsuarioEnum {

    SOLICITANTE("Médico Solicitante"),
    REGULADOR("Médico Regulador"),
    ADMINISTRADOR("Administrador do Sistema");

    private final String descricao;
}
