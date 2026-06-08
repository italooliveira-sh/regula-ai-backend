package br.com.petsaude.regula_ai_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * Relacao entre encaminhamento e procedimentos.
 */
@Entity
@Table(name = "encaminhamento_procedimento", indexes = {
    @Index(name = "idx_enc_proc_tipo", columnList = "tipo")
})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EncaminhamentoProcedimento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encaminhamento_id", nullable = false)
    private Encaminhamento encaminhamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "procedimento_id", nullable = false)
    private Procedimento procedimento;

    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo;

    @Column(name = "quantidade")
    private Integer quantidade;
}
