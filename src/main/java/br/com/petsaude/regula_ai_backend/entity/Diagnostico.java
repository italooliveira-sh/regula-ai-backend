package br.com.petsaude.regula_ai_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade de diagnostico CID.
 */
@Entity
@Table(name = "diagnosticos", indexes = {
    @Index(name = "idx_diagnosticos_cid", columnList = "cid_codigo")
})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Diagnostico {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "cid_codigo", nullable = false, unique = true, length = 10)
    private String cidCodigo;

    @Column(name = "descricao", length = 500)
    private String descricao;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
    }
}
