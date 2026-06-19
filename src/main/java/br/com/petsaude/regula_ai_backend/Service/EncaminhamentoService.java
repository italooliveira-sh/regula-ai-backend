package br.com.petsaude.regula_ai_backend.Service;

import br.com.petsaude.regula_ai_backend.DTO.EncaminhamentoDetalheDTO;
import br.com.petsaude.regula_ai_backend.DTO.EncaminhamentoResumoDTO;
import br.com.petsaude.regula_ai_backend.Repository.EncaminhamentoRepository;
import br.com.petsaude.regula_ai_backend.entity.Encaminhamento;
import br.com.petsaude.regula_ai_backend.exception.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EncaminhamentoService {

    private final EncaminhamentoRepository repo;

    public Page<EncaminhamentoResumoDTO> listar(
            String situacao,
            String prioridade,
            String codConsulta,
            String pacienteCodUsuario,
            String procedimentoCodigo,
            String estabelecimentoNome,
            LocalDateTime dtCadastroInicio,
            LocalDateTime dtCadastroFim,
            Pageable pageable
    ) {
        Specification<Encaminhamento> spec = Specification.where(null);

        if (situacao != null && !situacao.isBlank())
            spec = spec.and(EncaminhamentoSpec.comSituacao(situacao));
        if (prioridade != null && !prioridade.isBlank())
            spec = spec.and(EncaminhamentoSpec.comPrioridade(prioridade));
        if (codConsulta != null && !codConsulta.isBlank())
            spec = spec.and(EncaminhamentoSpec.comCodConsulta(codConsulta));
        if (pacienteCodUsuario != null && !pacienteCodUsuario.isBlank())
            spec = spec.and(EncaminhamentoSpec.comPacienteCodUsuario(pacienteCodUsuario));
        if (procedimentoCodigo != null && !procedimentoCodigo.isBlank())
            spec = spec.and(EncaminhamentoSpec.comProcedimentoCodigo(procedimentoCodigo));
        if (estabelecimentoNome != null && !estabelecimentoNome.isBlank())
            spec = spec.and(EncaminhamentoSpec.comEstabelecimentoNome(estabelecimentoNome));
        if (dtCadastroInicio != null)
            spec = spec.and(EncaminhamentoSpec.dtCadastroApos(dtCadastroInicio));
        if (dtCadastroFim != null)
            spec = spec.and(EncaminhamentoSpec.dtCadastroAntes(dtCadastroFim));

        return repo.findAll(spec, pageable).map(EncaminhamentoResumoDTO::from);
    }

    public EncaminhamentoDetalheDTO buscarPorId(UUID id) {
        return repo.findById(id)
                .map(EncaminhamentoDetalheDTO::from)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Encaminhamento não encontrado: id=" + id));
    }

    public EncaminhamentoDetalheDTO buscarPorCodConsulta(String cod) {
        return repo.findByCodConsulta(cod)
                .map(EncaminhamentoDetalheDTO::from)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Encaminhamento não encontrado: cod=" + cod));
    }
}
