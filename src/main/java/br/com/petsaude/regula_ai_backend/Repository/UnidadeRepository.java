package br.com.petsaude.regula_ai_backend.Repository;

import br.com.petsaude.regula_ai_backend.entity.Unidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UnidadeRepository extends JpaRepository<Unidade, UUID> {

    Optional<Unidade> findByNomeAndEndereco(String nome, String endereco);
}
