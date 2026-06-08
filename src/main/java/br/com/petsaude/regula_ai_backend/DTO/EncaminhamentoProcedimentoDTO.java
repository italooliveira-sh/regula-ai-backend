package br.com.petsaude.regula_ai_backend.DTO;

import br.com.petsaude.regula_ai_backend.entity.EncaminhamentoProcedimento;

public record EncaminhamentoProcedimentoDTO(ProcedimentoDTO procedimento, String tipo, Integer quantidade) {
    public static EncaminhamentoProcedimentoDTO from(EncaminhamentoProcedimento ep) {
        return new EncaminhamentoProcedimentoDTO(
                ProcedimentoDTO.from(ep.getProcedimento()),
                ep.getTipo(),
                ep.getQuantidade()
        );
    }
}
