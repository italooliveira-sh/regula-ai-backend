package br.com.petsaude.regula_ai_backend.DTO;

import br.com.petsaude.regula_ai_backend.entity.Diagnostico;

import java.util.UUID;

public record DiagnosticoDTO(UUID id, String cidCodigo, String descricao) {
    public static DiagnosticoDTO from(Diagnostico d) {
        return new DiagnosticoDTO(d.getId(), d.getCidCodigo(), d.getDescricao());
    }
}
