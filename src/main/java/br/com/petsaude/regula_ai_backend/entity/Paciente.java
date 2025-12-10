package br.com.petsaude.regula_ai_backend.entity;

import br.com.petsaude.regula_ai_backend.entity.enums.SexoEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade que representa um Paciente no sistema.
 * Mapeada para o recurso FHIR Patient.
 * 
 * @see <a href="https://hl7.org/fhir/R4/patient.html">FHIR Patient Resource</a>
 */
@Entity
@Table(name = "pacientes", indexes = {
    @Index(name = "idx_pacientes_cpf", columnList = "cpf"),
    @Index(name = "idx_pacientes_cartao_sus", columnList = "cartao_sus")
})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "nome", nullable = false, length = 255)
    private String nome;

    @Column(name = "cpf", nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(name = "cartao_sus", unique = true, length = 20)
    private String cartaoSus;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "sexo", length = 20)
    @Enumerated(EnumType.STRING)
    private SexoEnum sexo;

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
