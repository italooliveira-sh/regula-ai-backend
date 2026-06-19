package br.com.petsaude.regula_ai_backend.Repository;

import br.com.petsaude.regula_ai_backend.entity.EncaminhamentoProfissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EncaminhamentoProfissionalRepository extends JpaRepository<EncaminhamentoProfissional, UUID> {
}
