package br.com.petsaude.regula_ai_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Dados de agendamento vinculados ao encaminhamento.
 */
@Entity
@Table(name = "agendamentos")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encaminhamento_id", nullable = false)
    private Encaminhamento encaminhamento;

    @Column(name = "data_consulta")
    private LocalDateTime dataConsulta;

    @Column(name = "data_reservada")
    private LocalDateTime dataReservada;

    @Column(name = "limite_confirmacao_reserva")
    private LocalDateTime limiteConfirmacaoReserva;

    @Column(name = "dt_realizacao_agendamento")
    private LocalDateTime dtRealizacaoAgendamento;
}
