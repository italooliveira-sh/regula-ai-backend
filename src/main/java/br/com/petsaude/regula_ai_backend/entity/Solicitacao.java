package br.com.petsaude.regula_ai_backend.entity;

import br.com.petsaude.regula_ai_backend.entity.enums.StatusSolicitacaoEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "solicitacao")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Solicitacao {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidade_id", nullable = false)
    private Unidade unidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_solicitante_id", nullable = false)
    private Usuario medicoSolicitante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_regulador_id")
    private Usuario medicoRegulador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criado_por")
    private Usuario criadoPor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atualizado_por")
    private Usuario atualizadoPor;

    @Column(name = "data_solicitacao", nullable = false)
    private LocalDate dataSolicitacao;

    @Column(name = "codigo_cid", length = 10)
    private String codigoCid;

    @Column(name = "categoria_sub_categoria", length = 255)
    private String categoriaSubCategoria;

    @Column(name = "priorizacao", length = 50)
    private String priorizacao;

    @Column(name = "encaminhar_para_fila_atendimento", nullable = false)
    private Boolean encaminharParaFilaAtendimento;

    @Column(name = "status", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private StatusSolicitacaoEnum status;

    @Column(name = "justificativa", columnDefinition = "TEXT")
    private String justificativa;

    @Column(name = "observacoes_dados_clinicos", columnDefinition = "TEXT")
    private String observacoesDadosClinicos;

    @OneToMany(mappedBy = "solicitacao", cascade = CascadeType.ALL)
    private List<ItemProcedimentoSolicitado> itensSolicitados;
}
