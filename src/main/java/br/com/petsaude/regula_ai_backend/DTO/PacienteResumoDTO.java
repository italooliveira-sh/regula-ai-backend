package br.com.petsaude.regula_ai_backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO resumido para representação de Paciente em responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PacienteResumoDTO {

    private UUID id;
    private String nome;
    private String cpf;
    private String cartaoSus;
    private LocalDate dataNascimento;
}
