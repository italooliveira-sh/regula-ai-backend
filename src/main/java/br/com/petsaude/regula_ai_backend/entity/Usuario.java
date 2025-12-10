package br.com.petsaude.regula_ai_backend.entity;

import br.com.petsaude.regula_ai_backend.entity.enums.TipoUsuarioEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade que representa um Usuário (Profissional de Saúde) no sistema.
 * Mapeada para o recurso FHIR Practitioner.
 * 
 * @see <a href="https://hl7.org/fhir/R4/practitioner.html">FHIR Practitioner Resource</a>
 */
@Entity
@Table(name = "usuarios", indexes = {
        @Index(name = "idx_usuario_crm", columnList = "crm")
})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "nome", nullable = false, length = 255)
    private String nome;

    @Column(name = "crm", unique = true, length = 20)
    private String crm;

    @Column(name = "crm_uf", length = 2)
    private String crmUf;

    @Column(name = "cpf", nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @Column(name = "especialidade", length = 100)
    private String especialidade;

    @Column(name = "tipo", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private TipoUsuarioEnum tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidade_id")
    private Unidade unidade;

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
