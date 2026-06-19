package br.com.petsaude.regula_ai_backend.DTO;

import br.com.petsaude.regula_ai_backend.entity.EncaminhamentoEstabelecimento;

public record EncaminhamentoEstabelecimentoDTO(EstabelecimentoDTO estabelecimento, String papel) {
    public static EncaminhamentoEstabelecimentoDTO from(EncaminhamentoEstabelecimento e) {
        return new EncaminhamentoEstabelecimentoDTO(
                EstabelecimentoDTO.from(e.getEstabelecimento()),
                e.getPapel()
        );
    }
}
