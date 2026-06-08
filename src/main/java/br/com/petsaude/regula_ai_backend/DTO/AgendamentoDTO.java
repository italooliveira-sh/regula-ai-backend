package br.com.petsaude.regula_ai_backend.DTO;

import br.com.petsaude.regula_ai_backend.entity.Agendamento;

import java.time.LocalDateTime;
import java.util.UUID;

public record AgendamentoDTO(
        UUID id,
        LocalDateTime dataConsulta,
        LocalDateTime dataReservada,
        LocalDateTime limiteConfirmacaoReserva,
        LocalDateTime dtRealizacaoAgendamento
) {
    public static AgendamentoDTO from(Agendamento a) {
        return new AgendamentoDTO(
                a.getId(), a.getDataConsulta(), a.getDataReservada(),
                a.getLimiteConfirmacaoReserva(), a.getDtRealizacaoAgendamento()
        );
    }
}
