package br.com.petsaude.regula_ai_backend.Controller;

import br.com.petsaude.regula_ai_backend.DTO.PacienteDTO;
import br.com.petsaude.regula_ai_backend.DTO.PacienteDetalheDTO;
import br.com.petsaude.regula_ai_backend.Service.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
@Tag(name = "Pacientes", description = "Consulta de pacientes")
public class PacienteController {

    private final PacienteService service;

    @GetMapping
    @Operation(summary = "Lista pacientes com filtros e paginação")
    public Page<PacienteDTO> listar(
            @RequestParam(required = false) String codUsuario,
            @RequestParam(required = false) String municipio,
            @RequestParam(required = false) String bairro,
            @RequestParam(required = false) String equipe,
            @PageableDefault(size = 20, sort = "criadoEm") Pageable pageable
    ) {
        return service.listar(codUsuario, municipio, bairro, equipe, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalhe do paciente com seus encaminhamentos")
    public PacienteDetalheDTO buscarPorId(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/cod/{codUsuario}")
    @Operation(summary = "Detalhe do paciente por código de usuário")
    public PacienteDetalheDTO buscarPorCodUsuario(@PathVariable String codUsuario) {
        return service.buscarPorCodUsuario(codUsuario);
    }
}
