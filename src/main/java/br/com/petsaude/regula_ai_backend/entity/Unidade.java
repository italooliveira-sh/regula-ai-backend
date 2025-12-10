package br.com.petsaude.regula_ai_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade que representa uma Unidade de Saúde no sistema.
 * Mapeada para o recurso FHIR Organization/Location.
 * 
 * @see <a href="https://hl7.org/fhir/R4/organization.html">FHIR Organization Resource</a>
 */
@Entity
@Table(name = "unidades", indexes = {
    @Index(name = "idx_unidades_cnes", columnList = "cnes")
})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Unidade {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "nome", nullable = false, length = 255)
    private String nome;

    @Column(name = "cnes", unique = true, length = 10)
    private String cnes;

    @Column(name = "cnpj", unique = true, length = 18)
    private String cnpj;

    @Column(name = "tipo_unidade", length = 100)
    private String tipoUnidade;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "endereco", length = 500)
    private String endereco;

    @Column(name = "municipio", length = 100)
    private String municipio;

    @Column(name = "uf", length = 2)
    private String uf;

    @Column(name = "cep", length = 10)
    private String cep;

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
