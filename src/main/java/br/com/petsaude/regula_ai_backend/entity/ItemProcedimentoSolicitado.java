package br.com.petsaude.regula_ai_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "item_procedimento_solicitado")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemProcedimentoSolicitado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitacao_id", nullable = false)
    private Solicitacao solicitacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "procedimento_id", nullable = false)
    private Procedimento procedimento;

    @Column(name = "observacao", columnDefinition = "TEXT")
    private String observacao;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @Column(name = "inter", nullable = false)
    private Boolean inter = false;

    @Column(name = "realizar", nullable = false)
    private Boolean realizar = false;

    @Column(name = "cid10_item", length = 10)
    private String cid10Item;
}
