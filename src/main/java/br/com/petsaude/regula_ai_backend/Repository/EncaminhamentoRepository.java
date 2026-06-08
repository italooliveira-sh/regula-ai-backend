package br.com.petsaude.regula_ai_backend.Repository;

import br.com.petsaude.regula_ai_backend.entity.Encaminhamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EncaminhamentoRepository extends JpaRepository<Encaminhamento, UUID>, JpaSpecificationExecutor<Encaminhamento> {

    Optional<Encaminhamento> findByCodConsulta(String codConsulta);

    boolean existsByCodConsulta(String codConsulta);

    @Query("SELECT e.codConsulta FROM Encaminhamento e")
    List<String> findAllCodConsultas();

    List<Encaminhamento> findAllByCodConsultaIn(Collection<String> codConsultas);

    List<Encaminhamento> findByPacienteId(UUID pacienteId);

    @Query("SELECT e.situacao, COUNT(e) FROM Encaminhamento e WHERE e.situacao IS NOT NULL GROUP BY e.situacao ORDER BY COUNT(e) DESC")
    List<Object[]> countBySituacao();

    @Query("SELECT e.prioridade, COUNT(e) FROM Encaminhamento e WHERE e.prioridade IS NOT NULL GROUP BY e.prioridade ORDER BY COUNT(e) DESC")
    List<Object[]> countByPrioridade();
}
