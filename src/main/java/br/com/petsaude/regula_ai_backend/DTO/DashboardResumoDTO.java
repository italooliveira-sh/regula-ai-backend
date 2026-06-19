package br.com.petsaude.regula_ai_backend.DTO;

import java.util.List;

public record DashboardResumoDTO(
        long totalEncaminhamentos,
        long totalPacientes,
        List<ContadorDTO> porSituacao,
        List<ContadorDTO> porPrioridade,
        List<ProcedimentoFilaDTO> topProcedimentos
) {}
