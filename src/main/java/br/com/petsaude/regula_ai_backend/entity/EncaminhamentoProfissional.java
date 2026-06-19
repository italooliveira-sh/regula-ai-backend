package br.com.petsaude.regula_ai_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * Relacao entre encaminhamento e profissionais com papel.
 */
@Entity
@Table(name = "encaminhamento_profissional", indexes = {
    @Index(name = "idx_enc_prof_papel", columnList = "papel")
})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EncaminhamentoProfissional {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encaminhamento_id", nullable = false)
    private Encaminhamento encaminhamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profissional_id", nullable = false)
    private Usuario profissional;

    @Column(name = "papel", nullable = false, length = 50)
    private String papel;
}
