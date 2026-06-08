package br.com.petsaude.regula_ai_backend.DTO;

import br.com.petsaude.regula_ai_backend.entity.EncaminhamentoProfissional;

public record EncaminhamentoProfissionalDTO(ProfissionalDTO profissional, String papel) {
    public static EncaminhamentoProfissionalDTO from(EncaminhamentoProfissional ep) {
        return new EncaminhamentoProfissionalDTO(
                ProfissionalDTO.from(ep.getProfissional()),
                ep.getPapel()
        );
    }
}
