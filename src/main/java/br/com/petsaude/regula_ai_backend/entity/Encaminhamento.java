package br.com.petsaude.regula_ai_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entidade principal para encaminhamentos da fila.
 */
@Entity
@Table(name = "encaminhamentos", indexes = {
    @Index(name = "idx_encaminhamentos_paciente", columnList = "paciente_id"),
    @Index(name = "idx_encaminhamentos_prioridade", columnList = "prioridade"),
    @Index(name = "idx_encaminhamentos_situacao", columnList = "situacao"),
    @Index(name = "idx_encaminhamentos_dt_cadastro", columnList = "dt_cadastro")
})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Encaminhamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "cod_consulta", nullable = false, unique = true, length = 20)
    private String codConsulta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @Column(name = "dt_cadastro")
    private LocalDateTime dtCadastro;

    @Column(name = "prioridade", length = 100)
    private String prioridade;

    @Column(name = "posicao_fila")
    private Integer posicaoFila;

    @Column(name = "regulacao_flag")
    private Boolean regulacaoFlag;

    @Column(name = "busca_ativa_flag")
    private Boolean buscaAtivaFlag;

    @Column(name = "tipo", length = 100)
    private String tipo;

    @Column(name = "situacao", length = 200)
    private String situacao;

    @Column(name = "dt_ultima_alteracao")
    private LocalDateTime dtUltimaAlteracao;

    @Column(name = "motivo_encaminhamento", columnDefinition = "TEXT")
    private String motivoEncaminhamento;

    @Column(name = "origem_solicitacao", length = 200)
    private String origemSolicitacao;

    @Column(name = "processo_automatico_flag")
    private Boolean processoAutomaticoFlag;

    @Column(name = "solicitacao_leito")
    private Boolean solicitacaoLeito;

    @Column(name = "data_controle")
    private LocalDate dataControle;

    @Column(name = "perfil", length = 100)
    private String perfil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cid_principal_id")
    private Diagnostico cidPrincipal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cid_prioritario_id")
    private Diagnostico cidPrioritario;

    @OneToMany(mappedBy = "encaminhamento", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<EncaminhamentoEstabelecimento> estabelecimentos = new ArrayList<>();

    @OneToMany(mappedBy = "encaminhamento", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<EncaminhamentoProfissional> profissionais = new ArrayList<>();

    @OneToMany(mappedBy = "encaminhamento", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<EncaminhamentoProcedimento> procedimentos = new ArrayList<>();

    @OneToOne(mappedBy = "encaminhamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private Agendamento agendamento;

    @OneToOne(mappedBy = "encaminhamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private Negativa negativa;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
    }
}
