package br.com.petsaude.regula_ai_backend.Controller;

import br.com.petsaude.regula_ai_backend.DTO.DiagnosticoDTO;
import br.com.petsaude.regula_ai_backend.Service.ReferenceDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diagnosticos")
@RequiredArgsConstructor
@Tag(name = "Diagnósticos", description = "Catálogo de diagnósticos CID")
public class DiagnosticoController {

    private final ReferenceDataService service;

    @GetMapping
    @Operation(summary = "Lista todos os diagnósticos")
    public List<DiagnosticoDTO> listar() {
        return service.listarDiagnosticos();
    }

    @GetMapping("/cid/{cidCodigo}")
    @Operation(summary = "Busca diagnóstico pelo código CID")
    public DiagnosticoDTO buscarPorCid(@PathVariable String cidCodigo) {
        return service.buscarPorCid(cidCodigo);
    }
}
