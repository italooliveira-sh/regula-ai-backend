package br.com.petsaude.regula_ai_backend.Service;

import br.com.petsaude.regula_ai_backend.DTO.ContadorDTO;
import br.com.petsaude.regula_ai_backend.DTO.DashboardResumoDTO;
import br.com.petsaude.regula_ai_backend.DTO.ProcedimentoFilaDTO;
import br.com.petsaude.regula_ai_backend.Repository.EncaminhamentoProcedimentoRepository;
import br.com.petsaude.regula_ai_backend.Repository.EncaminhamentoRepository;
import br.com.petsaude.regula_ai_backend.Repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private static final int TOP_PROCEDIMENTOS = 10;

    private final EncaminhamentoRepository encaminhamentoRepo;
    private final PacienteRepository pacienteRepo;
    private final EncaminhamentoProcedimentoRepository procedimentoRepo;

    public DashboardResumoDTO resumo() {
        long totalEncaminhamentos = encaminhamentoRepo.count();
        long totalPacientes = pacienteRepo.count();

        List<ContadorDTO> porSituacao = encaminhamentoRepo.countBySituacao().stream()
                .map(row -> new ContadorDTO((String) row[0], (Long) row[1]))
                .toList();

        List<ContadorDTO> porPrioridade = encaminhamentoRepo.countByPrioridade().stream()
                .map(row -> new ContadorDTO((String) row[0], (Long) row[1]))
                .toList();

        List<ProcedimentoFilaDTO> topProcedimentos = procedimentoRepo
                .findTopProcedimentos(PageRequest.of(0, TOP_PROCEDIMENTOS))
                .stream()
                .map(row -> new ProcedimentoFilaDTO((String) row[0], (String) row[1], (Long) row[2]))
                .toList();

        return new DashboardResumoDTO(totalEncaminhamentos, totalPacientes,
                porSituacao, porPrioridade, topProcedimentos);
    }
}
