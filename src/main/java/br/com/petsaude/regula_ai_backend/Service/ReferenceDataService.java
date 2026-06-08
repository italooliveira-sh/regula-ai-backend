package br.com.petsaude.regula_ai_backend.Service;

import br.com.petsaude.regula_ai_backend.DTO.DiagnosticoDTO;
import br.com.petsaude.regula_ai_backend.DTO.EstabelecimentoDTO;
import br.com.petsaude.regula_ai_backend.DTO.ProcedimentoDTO;
import br.com.petsaude.regula_ai_backend.Repository.DiagnosticoRepository;
import br.com.petsaude.regula_ai_backend.Repository.ProcedimentoRepository;
import br.com.petsaude.regula_ai_backend.Repository.UnidadeRepository;
import br.com.petsaude.regula_ai_backend.exception.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReferenceDataService {

    private final ProcedimentoRepository procedimentoRepo;
    private final UnidadeRepository unidadeRepo;
    private final DiagnosticoRepository diagnosticoRepo;

    public List<ProcedimentoDTO> listarProcedimentos() {
        return procedimentoRepo.findAll().stream().map(ProcedimentoDTO::from).toList();
    }

    public List<EstabelecimentoDTO> listarEstabelecimentos() {
        return unidadeRepo.findAll().stream().map(EstabelecimentoDTO::from).toList();
    }

    public List<DiagnosticoDTO> listarDiagnosticos() {
        return diagnosticoRepo.findAll().stream().map(DiagnosticoDTO::from).toList();
    }

    public DiagnosticoDTO buscarPorCid(String cid) {
        return diagnosticoRepo.findByCidCodigo(cid)
                .map(DiagnosticoDTO::from)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Diagnóstico não encontrado: cid=" + cid));
    }
}
