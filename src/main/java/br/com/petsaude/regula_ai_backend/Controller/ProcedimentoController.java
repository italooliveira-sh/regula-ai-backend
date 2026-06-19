package br.com.petsaude.regula_ai_backend.Controller;

import br.com.petsaude.regula_ai_backend.DTO.ProcedimentoDTO;
import br.com.petsaude.regula_ai_backend.Service.ReferenceDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/procedimentos")
@RequiredArgsConstructor
@Tag(name = "Procedimentos", description = "Catálogo de procedimentos médicos")
public class ProcedimentoController {

    private final ReferenceDataService service;

    @GetMapping
    @Operation(summary = "Lista todos os procedimentos")
    public List<ProcedimentoDTO> listar() {
        return service.listarProcedimentos();
    }
}
