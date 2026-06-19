package br.com.petsaude.regula_ai_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * Relacao entre encaminhamento e estabelecimentos com papel.
 */
@Entity
@Table(name = "encaminhamento_estabelecimento", indexes = {
    @Index(name = "idx_enc_estab_papel", columnList = "papel")
})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EncaminhamentoEstabelecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encaminhamento_id", nullable = false)
    private Encaminhamento encaminhamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estabelecimento_id", nullable = false)
    private Unidade estabelecimento;

    @Column(name = "papel", nullable = false, length = 50)
    private String papel;
}
