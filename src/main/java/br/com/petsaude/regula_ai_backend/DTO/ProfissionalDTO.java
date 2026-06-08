package br.com.petsaude.regula_ai_backend.DTO;

import br.com.petsaude.regula_ai_backend.entity.Usuario;

import java.util.UUID;

public record ProfissionalDTO(UUID id, String nome) {
    public static ProfissionalDTO from(Usuario u) {
        return new ProfissionalDTO(u.getId(), u.getNome());
    }
}
