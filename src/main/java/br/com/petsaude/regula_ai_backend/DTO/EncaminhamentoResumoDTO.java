package br.com.petsaude.regula_ai_backend.DTO;

import br.com.petsaude.regula_ai_backend.entity.Encaminhamento;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record EncaminhamentoResumoDTO(
        UUID id,
        String codConsulta,
        String prioridade,
        String situacao,
        Integer posicaoFila,
        String tipo,
        LocalDateTime dtCadastro,
        LocalDateTime dtUltimaAlteracao,
        LocalDate dataControle,
        String pacienteCodUsuario,
        LocalDateTime criadoEm
) {
    public static EncaminhamentoResumoDTO from(Encaminhamento e) {
        return new EncaminhamentoResumoDTO(
                e.getId(),
                e.getCodConsulta(),
                e.getPrioridade(),
                e.getSituacao(),
                e.getPosicaoFila(),
                e.getTipo(),
                e.getDtCadastro(),
                e.getDtUltimaAlteracao(),
                e.getDataControle(),
                e.getPaciente() != null ? e.getPaciente().getCodUsuario() : null,
                e.getCriadoEm()
        );
    }
}
