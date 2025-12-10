package br.com.petsaude.regula_ai_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * Entidade que representa um Item de Solicitação de Serviço (ServiceRequest Detail).
 * 
 * PADRÃO SNAPSHOT: Esta classe armazena uma CÓPIA dos dados do procedimento
 * no momento da criação do pedido.
 * 
 * Isso significa que mesmo se o catálogo de procedimentos for atualizado,
 * os dados históricos da solicitação permanecem inalterados.
 */
@Entity
@Table(name = "service_request_details", indexes = {
    @Index(name = "idx_srd_solicitacao", columnList = "service_request_id"),
    @Index(name = "idx_srd_code", columnList = "code")
})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemSolicitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_request_id", nullable = false)
    private Solicitacao solicitacao;

    /**
     * Código do procedimento/exame (ex: 0205020097).
     * Copiado do catálogo no momento da criação.
     */
    @Column(name = "code", nullable = false, length = 20)
    private String code;

    /**
     * Nome/descrição do procedimento no momento do pedido.
     * Copiado do catálogo no momento da criação.
     */
    @Column(name = "display", nullable = false, length = 500)
    private String display;

    /**
     * Sistema de codificação de origem (ex: SIGTAP, TUSS, LOINC).
     * Copiado do catálogo no momento da criação.
     */
    @Column(name = "code_system", nullable = false, length = 100)
    private String codeSystem;

    /**
     * Quantidade solicitada do procedimento.
     */
    @Column(name = "quantity", nullable = false)
    @Builder.Default
    private Integer quantity = 1;

}
