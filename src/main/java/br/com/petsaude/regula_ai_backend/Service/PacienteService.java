package br.com.petsaude.regula_ai_backend.Service;

import br.com.petsaude.regula_ai_backend.DTO.EncaminhamentoResumoDTO;
import br.com.petsaude.regula_ai_backend.DTO.PacienteDTO;
import br.com.petsaude.regula_ai_backend.DTO.PacienteDetalheDTO;
import br.com.petsaude.regula_ai_backend.Repository.EncaminhamentoRepository;
import br.com.petsaude.regula_ai_backend.Repository.PacienteRepository;
import br.com.petsaude.regula_ai_backend.entity.Paciente;
import br.com.petsaude.regula_ai_backend.exception.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PacienteService {

    private final PacienteRepository pacienteRepo;
    private final EncaminhamentoRepository encaminhamentoRepo;

    public Page<PacienteDTO> listar(
            String codUsuario,
            String municipio,
            String bairro,
            String equipe,
            Pageable pageable
    ) {
        Specification<Paciente> spec = Specification.where(null);

        if (codUsuario != null && !codUsuario.isBlank())
            spec = spec.and((r, q, cb) -> cb.equal(r.get("codUsuario"), codUsuario));
        if (municipio != null && !municipio.isBlank())
            spec = spec.and((r, q, cb) -> cb.like(cb.lower(r.get("municipio")), "%" + municipio.toLowerCase() + "%"));
        if (bairro != null && !bairro.isBlank())
            spec = spec.and((r, q, cb) -> cb.like(cb.lower(r.get("bairro")), "%" + bairro.toLowerCase() + "%"));
        if (equipe != null && !equipe.isBlank())
            spec = spec.and((r, q, cb) -> cb.like(cb.lower(r.get("equipe")), "%" + equipe.toLowerCase() + "%"));

        return pacienteRepo.findAll(spec, pageable).map(PacienteDTO::from);
    }

    public PacienteDetalheDTO buscarPorId(UUID id) {
        Paciente p = pacienteRepo.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado: id=" + id));
        return toDetalhe(p);
    }

    public PacienteDetalheDTO buscarPorCodUsuario(String cod) {
        Paciente p = pacienteRepo.findByCodUsuario(cod)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado: cod=" + cod));
        return toDetalhe(p);
    }

    private PacienteDetalheDTO toDetalhe(Paciente p) {
        List<EncaminhamentoResumoDTO> encaminhamentos = encaminhamentoRepo
                .findByPacienteId(p.getId())
                .stream()
                .map(EncaminhamentoResumoDTO::from)
                .toList();
        return PacienteDetalheDTO.from(p, encaminhamentos);
    }
}
