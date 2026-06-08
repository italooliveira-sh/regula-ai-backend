package br.com.petsaude.regula_ai_backend.DTO;

import br.com.petsaude.regula_ai_backend.entity.Procedimento;

import java.util.UUID;

public record ProcedimentoDTO(UUID id, String codigo, String descricao) {
    public static ProcedimentoDTO from(Procedimento p) {
        return new ProcedimentoDTO(p.getId(), p.getCodigo(), p.getDescricao());
    }
}
