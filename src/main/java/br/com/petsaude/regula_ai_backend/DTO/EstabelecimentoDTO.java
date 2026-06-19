package br.com.petsaude.regula_ai_backend.DTO;

import br.com.petsaude.regula_ai_backend.entity.Unidade;

import java.util.UUID;

public record EstabelecimentoDTO(UUID id, String nome, String endereco) {
    public static EstabelecimentoDTO from(Unidade u) {
        return new EstabelecimentoDTO(u.getId(), u.getNome(), u.getEndereco());
    }
}
