package br.com.petsaude.regula_ai_backend.DTO;

import br.com.petsaude.regula_ai_backend.entity.Paciente;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PacienteDetalheDTO(
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
        LocalDateTime atualizadoEm,
        List<EncaminhamentoResumoDTO> encaminhamentos
) {
    public static PacienteDetalheDTO from(Paciente p, List<EncaminhamentoResumoDTO> encaminhamentos) {
        return new PacienteDetalheDTO(
                p.getId(), p.getCodUsuario(), p.getNumeroProntuario(),
                p.getSexo(), p.getNascimento(), p.getIdade(),
                p.getBairro(), p.getMunicipio(), p.getMicroarea(),
                p.getEquipe(), p.getCriadoEm(), p.getAtualizadoEm(),
                encaminhamentos
        );
    }
}
