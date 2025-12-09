package br.com.petsaude.regula_ai_backend.entity;

import br.com.petsaude.regula_ai_backend.entity.enums.IntentEnum;
import br.com.petsaude.regula_ai_backend.entity.enums.PriorityEnum;
import br.com.petsaude.regula_ai_backend.entity.enums.StatusFhirEnum;
import br.com.petsaude.regula_ai_backend.entity.enums.StatusSolicitacaoEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entidade que representa uma Solicitação de Serviço de Saúde.
 * Mapeada para o recurso FHIR ServiceRequest.
 * 
 * Esta classe segue o padrão "Flattened" para armazenar triplas de código
 * (system, code, display) diretamente, facilitando a geração de JSONs FHIR.
 * 
 * @see <a href="https://hl7.org/fhir/R4/servicerequest.html">FHIR ServiceRequest Resource</a>
 */
@Entity
@Table(name = "service_requests", indexes = {
    @Index(name = "idx_sr_paciente", columnList = "paciente_id"),
    @Index(name = "idx_sr_requester", columnList = "requester_id"),
    @Index(name = "idx_sr_performer", columnList = "performer_id"),
    @Index(name = "idx_sr_status_fhir", columnList = "status"),
    @Index(name = "idx_sr_status_regulacao", columnList = "status_regulacao"),
    @Index(name = "idx_sr_authored_on", columnList = "authored_on")
})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Solicitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * FHIR intent: Indica a intenção do pedido.
     */
    @Column(name = "intent", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private IntentEnum intent = IntentEnum.ORDER;

    /**
     * FHIR status: Status do ciclo de vida do pedido.
     * Valores: draft, active, on-hold, revoked, completed, entered-in-error, unknown.
     */
    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatusFhirEnum statusFhir = StatusFhirEnum.DRAFT;

    /**
     * FHIR priority: Prioridade do pedido.
     * Valores: routine, urgent, asap, stat.
     */
    @Column(name = "priority", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PriorityEnum priority = PriorityEnum.ROUTINE;

    /**
     * FHIR authoredOn: Data/hora em que o pedido foi criado.
     */
    @Column(name = "authored_on", nullable = false)
    private LocalDateTime authoredOn;

    /**
     * Sistema de codificação da categoria (ex: http://snomed.info/sct).
     */
    @Column(name = "category_system", length = 255)
    private String categorySystem;

    /**
     * Código da categoria.
     */
    @Column(name = "category_code", length = 50)
    private String categoryCode;

    /**
     * Descrição legível da categoria.
     */
    @Column(name = "category_display", length = 255)
    private String categoryDisplay;

    /**
     * Sistema de codificação do diagnóstico (ex: http://hl7.org/fhir/sid/icd-10).
     */
    @Column(name = "reason_system", length = 255)
    private String reasonSystem;

    /**
     * Código CID-10 do diagnóstico principal.
     */
    @Column(name = "reason_code", length = 10)
    private String reasonCode;

    /**
     * Descrição legível do diagnóstico.
     */
    @Column(name = "reason_display", length = 500)
    private String reasonDisplay;

    /**
     * Notas/observações clínicas.
     * Mapeado para FHIR ServiceRequest.note.
     */
    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    /**
     * Flag para encaminhar para fila de atendimento.
     * Campo de negócio específico do sistema de regulação.
     */
    @Column(name = "queue_flag", nullable = false)
    @Builder.Default
    private Boolean queueFlag = false;

    /**
     * Status interno do processo de regulação.
     * Separado do status FHIR para controle de negócio.
     */
    @Column(name = "status_regulacao", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatusSolicitacaoEnum statusRegulacao = StatusSolicitacaoEnum.PENDENTE;

    /**
     * Paciente (FHIR ServiceRequest.subject).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    /**
     * Médico solicitante (FHIR ServiceRequest.requester).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private Usuario medicoSolicitante;

    /**
     * Unidade executante (FHIR ServiceRequest.performer).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performer_id", nullable = false)
    private Unidade unidadeExecutante;

    /**
     * Médico regulador (opcional, preenchido durante a regulação).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regulador_id")
    private Usuario medicoRegulador;

    /**
     * Itens da solicitação (procedimentos/exames).
     * CascadeType.ALL garante que os itens sejam persistidos junto com a solicitação.
     */
    @OneToMany(mappedBy = "solicitacao", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ItemSolicitacao> itens = new ArrayList<>();

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
        if (this.authoredOn == null) {
            this.authoredOn = LocalDateTime.now();
        }
    }

    public void addItem(ItemSolicitacao item) {
        itens.add(item);
        item.setSolicitacao(this);
    }

    public void removeItem(ItemSolicitacao item) {
        itens.remove(item);
        item.setSolicitacao(null);
    }
}
