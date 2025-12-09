package br.com.petsaude.regula_ai_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade de Catálogo/Dicionário de Procedimentos.
 * Os dados são copiados para ItemSolicitacao no momento da criação do pedido,
 * garantindo imutabilidade do prontuário.
 */
@Entity
@Table(name = "procedimentos", indexes = {
    @Index(name = "idx_procedimentos_codigo", columnList = "codigo"),
    @Index(name = "idx_procedimentos_sistema", columnList = "sistema")
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

    @Column(name = "nome", nullable = false, length = 500)
    private String nome;

    @Column(name = "sistema", nullable = false, length = 50)
    private String sistema;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "grupo", length = 100)
    private String grupo;

    @Column(name = "subgrupo", length = 100)
    private String subgrupo;

    @Column(name = "ativo", nullable = false)
    @Builder.Default
    private Boolean ativo = true;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
    }
}
