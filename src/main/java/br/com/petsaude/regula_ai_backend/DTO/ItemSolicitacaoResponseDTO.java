package br.com.petsaude.regula_ai_backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO de resposta para item de solicitação.
 * Contém os dados do snapshot (code, display, codeSystem) já populados.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemSolicitacaoResponseDTO {

    private UUID id;

    // ==========================================
    // Dados do Snapshot
    // ==========================================

    /**
     * Código do procedimento (snapshot).
     */
    private String code;

    /**
     * Nome/descrição do procedimento (snapshot).
     */
    private String display;

    /**
     * Sistema de codificação (snapshot).
     */
    private String codeSystem;

    // ==========================================
    // Dados do item
    // ==========================================

    private Integer quantity;

    private String note;
}
