package br.com.petsaude.regula_ai_backend.Repository;

import br.com.petsaude.regula_ai_backend.entity.Solicitacao;
import br.com.petsaude.regula_ai_backend.entity.enums.StatusFhirEnum;
import br.com.petsaude.regula_ai_backend.entity.enums.StatusSolicitacaoEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, UUID> {

    List<Solicitacao> findByPacienteId(UUID pacienteId);

    Page<Solicitacao> findByPacienteId(UUID pacienteId, Pageable pageable);

    List<Solicitacao> findByMedicoSolicitanteId(UUID medicoSolicitanteId);

    Page<Solicitacao> findByMedicoSolicitanteId(UUID medicoSolicitanteId, Pageable pageable);

    List<Solicitacao> findByMedicoReguladorId(UUID medicoReguladorId);

    List<Solicitacao> findByUnidadeExecutanteId(UUID unidadeExecutanteId);

    Page<Solicitacao> findByUnidadeExecutanteId(UUID unidadeExecutanteId, Pageable pageable);

    List<Solicitacao> findByStatusFhir(StatusFhirEnum statusFhir);

    Page<Solicitacao> findByStatusFhir(StatusFhirEnum statusFhir, Pageable pageable);

    List<Solicitacao> findByStatusRegulacao(StatusSolicitacaoEnum statusRegulacao);

    Page<Solicitacao> findByStatusRegulacao(StatusSolicitacaoEnum statusRegulacao, Pageable pageable);

    List<Solicitacao> findByAuthoredOnBetween(LocalDateTime inicio, LocalDateTime fim);

    Page<Solicitacao> findByAuthoredOnBetween(LocalDateTime inicio, LocalDateTime fim, Pageable pageable);

    List<Solicitacao> findByReasonCode(String reasonCode);

    List<Solicitacao> findByQueueFlagTrue();

    Page<Solicitacao> findByQueueFlagTrueAndStatusRegulacao(StatusSolicitacaoEnum statusRegulacao, Pageable pageable);

    @Query("SELECT s FROM Solicitacao s WHERE s.statusRegulacao = :status ORDER BY s.authoredOn ASC")
    List<Solicitacao> findPendentesOrdenadoPorData(@Param("status") StatusSolicitacaoEnum status);

    @Query("SELECT s.statusRegulacao, COUNT(s) FROM Solicitacao s GROUP BY s.statusRegulacao")
    List<Object[]> countByStatusRegulacao();

    @Query("SELECT s FROM Solicitacao s " +
           "LEFT JOIN FETCH s.paciente " +
           "LEFT JOIN FETCH s.medicoSolicitante " +
           "LEFT JOIN FETCH s.unidadeExecutante " +
           "LEFT JOIN FETCH s.itens " +
           "WHERE s.id = :id")
    Solicitacao findByIdWithRelationships(@Param("id") UUID id);
}
