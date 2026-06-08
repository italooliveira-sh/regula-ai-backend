package br.com.petsaude.regula_ai_backend.Repository;

import br.com.petsaude.regula_ai_backend.entity.EncaminhamentoProcedimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EncaminhamentoProcedimentoRepository extends JpaRepository<EncaminhamentoProcedimento, UUID> {
}
