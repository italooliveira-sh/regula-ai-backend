package br.com.petsaude.regula_ai_backend.Repository;

import br.com.petsaude.regula_ai_backend.entity.Procedimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProcedimentoRepository extends JpaRepository<Procedimento, UUID> {

    Optional<Procedimento> findByCodigo(String codigo);

    List<Procedimento> findByAtivoTrue();

    List<Procedimento> findBySistema(String sistema);

    List<Procedimento> findByGrupo(String grupo);

    List<Procedimento> findByNomeContainingIgnoreCase(String nome);

    boolean existsByCodigo(String codigo);
}
