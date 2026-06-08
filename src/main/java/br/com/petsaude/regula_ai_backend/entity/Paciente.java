package br.com.petsaude.regula_ai_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade que representa um paciente conforme a estrutura da fila.
 */
@Entity
@Table(name = "pacientes", indexes = {
    @Index(name = "idx_pacientes_cod_usuario", columnList = "cod_usuario")
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

    @Column(name = "cod_usuario", nullable = false, unique = true, length = 20)
    private String codUsuario;

    @Column(name = "numero_prontuario", length = 30)
    private String numeroProntuario;

    @Column(name = "sexo", length = 10)
    private String sexo;

    @Column(name = "nascimento")
    private LocalDate nascimento;

    @Column(name = "idade")
    private Integer idade;

    @Column(name = "bairro", length = 120)
    private String bairro;

    @Column(name = "municipio", length = 120)
    private String municipio;

    @Column(name = "microarea", length = 120)
    private String microarea;

    @Column(name = "equipe", length = 120)
    private String equipe;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
    }
}
