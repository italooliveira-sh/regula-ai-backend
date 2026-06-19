package br.com.petsaude.regula_ai_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Dados de negativa/cancelamento vinculados ao encaminhamento.
 */
@Entity
@Table(name = "negativas")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Negativa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encaminhamento_id", nullable = false)
    private Encaminhamento encaminhamento;

    @Column(name = "motivo_negativa", length = 500)
    private String motivoNegativa;

    @Column(name = "data_negativa")
    private LocalDate dataNegativa;

    @Column(name = "justificativa_negativa", columnDefinition = "TEXT")
    private String justificativaNegativa;

    @Column(name = "responsavel_negativa", length = 255)
    private String responsavelNegativa;

    @Column(name = "justificativa_cancelamento", columnDefinition = "TEXT")
    private String justificativaCancelamento;
}
