package br.com.petsaude.regula_ai_backend.Controller;

import br.com.petsaude.regula_ai_backend.DTO.DashboardResumoDTO;
import br.com.petsaude.regula_ai_backend.Service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Estatísticas e resumo da fila")
public class DashboardController {

    private final DashboardService service;

    @GetMapping("/resumo")
    @Operation(summary = "Resumo geral: totais, contagens por situação, prioridade e top procedimentos")
    public DashboardResumoDTO resumo() {
        return service.resumo();
    }
}
