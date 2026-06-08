package br.com.petsaude.regula_ai_backend.DTO;

import br.com.petsaude.regula_ai_backend.entity.Paciente;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record PacienteDTO(
        UUID id,
        String codUsuario,
        String numeroProntuario,
        String sexo,
        LocalDate nascimento,
        Integer idade,
        String bairro,
        String municipio,
        String microarea,
        String equipe,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {
    public static PacienteDTO from(Paciente p) {
        return new PacienteDTO(
                p.getId(), p.getCodUsuario(), p.getNumeroProntuario(),
                p.getSexo(), p.getNascimento(), p.getIdade(),
                p.getBairro(), p.getMunicipio(), p.getMicroarea(),
                p.getEquipe(), p.getCriadoEm(), p.getAtualizadoEm()
        );
    }
}
