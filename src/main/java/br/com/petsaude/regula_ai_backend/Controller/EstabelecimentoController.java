package br.com.petsaude.regula_ai_backend.Controller;

import br.com.petsaude.regula_ai_backend.DTO.EstabelecimentoDTO;
import br.com.petsaude.regula_ai_backend.Service.ReferenceDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/estabelecimentos")
@RequiredArgsConstructor
@Tag(name = "Estabelecimentos", description = "Catálogo de unidades de saúde")
public class EstabelecimentoController {

    private final ReferenceDataService service;

    @GetMapping
    @Operation(summary = "Lista todos os estabelecimentos de saúde")
    public List<EstabelecimentoDTO> listar() {
        return service.listarEstabelecimentos();
    }
}
