package br.com.petsaude.regula_ai_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade de catalogo de procedimentos conforme o CSV.
 */
@Entity
@Table(name = "procedimentos", indexes = {
    @Index(name = "idx_procedimentos_codigo", columnList = "codigo")
})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Procedimento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "codigo", nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(name = "descricao", length = 500)
    private String descricao;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
    }
}
