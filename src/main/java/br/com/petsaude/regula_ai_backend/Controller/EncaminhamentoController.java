package br.com.petsaude.regula_ai_backend.Controller;

import br.com.petsaude.regula_ai_backend.DTO.EncaminhamentoDetalheDTO;
import br.com.petsaude.regula_ai_backend.DTO.EncaminhamentoResumoDTO;
import br.com.petsaude.regula_ai_backend.Service.EncaminhamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/encaminhamentos")
@RequiredArgsConstructor
@Tag(name = "Encaminhamentos", description = "Consulta da fila de encaminhamentos")
public class EncaminhamentoController {

    private final EncaminhamentoService service;

    @GetMapping
    @Operation(summary = "Lista encaminhamentos com filtros e paginação")
    public Page<EncaminhamentoResumoDTO> listar(
            @RequestParam(required = false) String situacao,
            @RequestParam(required = false) String prioridade,
            @RequestParam(required = false) String codConsulta,
            @RequestParam(required = false) String pacienteCodUsuario,
            @RequestParam(required = false) String procedimentoCodigo,
            @RequestParam(required = false) String estabelecimentoNome,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dtCadastroInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dtCadastroFim,
            @PageableDefault(size = 20, sort = "dtCadastro") Pageable pageable
    ) {
        return service.listar(situacao, prioridade, codConsulta, pacienteCodUsuario,
                procedimentoCodigo, estabelecimentoNome, dtCadastroInicio, dtCadastroFim, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalhe completo do encaminhamento por ID")
    public EncaminhamentoDetalheDTO buscarPorId(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/cod/{codConsulta}")
    @Operation(summary = "Detalhe completo do encaminhamento por código de consulta")
    public EncaminhamentoDetalheDTO buscarPorCodConsulta(@PathVariable String codConsulta) {
        return service.buscarPorCodConsulta(codConsulta);
    }
}
