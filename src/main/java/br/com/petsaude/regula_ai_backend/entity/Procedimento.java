package br.com.petsaude.regula_ai_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "procedimento")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Procedimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    @Column(name = "criterios_encaminhamento", columnDefinition = "jsonb")
    private String criteriosEncaminhamento;

    @OneToMany(mappedBy = "procedimento")
    private List<ItemProcedimentoSolicitado> itensSolicitados;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }
}
