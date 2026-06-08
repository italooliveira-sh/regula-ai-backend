package br.com.petsaude.regula_ai_backend.Service;

import br.com.petsaude.regula_ai_backend.entity.Encaminhamento;
import br.com.petsaude.regula_ai_backend.entity.EncaminhamentoEstabelecimento;
import br.com.petsaude.regula_ai_backend.entity.EncaminhamentoProcedimento;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public final class EncaminhamentoSpec {

    private EncaminhamentoSpec() {}

    public static Specification<Encaminhamento> comSituacao(String situacao) {
        return (root, query, cb) -> cb.equal(cb.lower(root.get("situacao")), situacao.toLowerCase());
    }

    public static Specification<Encaminhamento> comPrioridade(String prioridade) {
        return (root, query, cb) -> cb.equal(cb.lower(root.get("prioridade")), prioridade.toLowerCase());
    }

    public static Specification<Encaminhamento> comCodConsulta(String cod) {
        return (root, query, cb) -> cb.equal(root.get("codConsulta"), cod);
    }

    public static Specification<Encaminhamento> comPacienteCodUsuario(String cod) {
        return (root, query, cb) -> cb.equal(root.get("paciente").get("codUsuario"), cod);
    }

    public static Specification<Encaminhamento> dtCadastroApos(LocalDateTime inicio) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("dtCadastro"), inicio);
    }

    public static Specification<Encaminhamento> dtCadastroAntes(LocalDateTime fim) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("dtCadastro"), fim);
    }

    public static Specification<Encaminhamento> comProcedimentoCodigo(String codigo) {
        return (root, query, cb) -> {
            Subquery<EncaminhamentoProcedimento> sq = query.subquery(EncaminhamentoProcedimento.class);
            Root<EncaminhamentoProcedimento> ep = sq.from(EncaminhamentoProcedimento.class);
            sq.select(ep).where(
                    cb.equal(ep.get("encaminhamento"), root),
                    cb.equal(ep.get("procedimento").get("codigo"), codigo)
            );
            return cb.exists(sq);
        };
    }

    public static Specification<Encaminhamento> comEstabelecimentoNome(String nome) {
        return (root, query, cb) -> {
            Subquery<EncaminhamentoEstabelecimento> sq = query.subquery(EncaminhamentoEstabelecimento.class);
            Root<EncaminhamentoEstabelecimento> ee = sq.from(EncaminhamentoEstabelecimento.class);
            sq.select(ee).where(
                    cb.equal(ee.get("encaminhamento"), root),
                    cb.like(cb.lower(ee.get("estabelecimento").get("nome")), "%" + nome.toLowerCase() + "%")
            );
            return cb.exists(sq);
        };
    }
}
