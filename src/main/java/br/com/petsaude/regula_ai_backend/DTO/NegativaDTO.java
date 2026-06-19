package br.com.petsaude.regula_ai_backend.DTO;

import br.com.petsaude.regula_ai_backend.entity.Negativa;

import java.time.LocalDate;
import java.util.UUID;

public record NegativaDTO(
        UUID id,
        String motivoNegativa,
        LocalDate dataNegativa,
        String justificativaNegativa,
        String responsavelNegativa,
        String justificativaCancelamento
) {
    public static NegativaDTO from(Negativa n) {
        return new NegativaDTO(
                n.getId(), n.getMotivoNegativa(), n.getDataNegativa(),
                n.getJustificativaNegativa(), n.getResponsavelNegativa(),
                n.getJustificativaCancelamento()
        );
    }
}
