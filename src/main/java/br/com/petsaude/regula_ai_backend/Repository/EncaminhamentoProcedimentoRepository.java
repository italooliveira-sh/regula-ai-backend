package br.com.petsaude.regula_ai_backend.Repository;

import br.com.petsaude.regula_ai_backend.entity.EncaminhamentoProcedimento;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EncaminhamentoProcedimentoRepository extends JpaRepository<EncaminhamentoProcedimento, UUID> {

    @Query("SELECT ep.procedimento.codigo, ep.procedimento.descricao, COUNT(ep) " +
           "FROM EncaminhamentoProcedimento ep " +
           "GROUP BY ep.procedimento.codigo, ep.procedimento.descricao " +
           "ORDER BY COUNT(ep) DESC")
    List<Object[]> findTopProcedimentos(Pageable pageable);
}
