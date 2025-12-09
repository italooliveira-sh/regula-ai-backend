package br.com.petsaude.regula_ai_backend.Repository;

import br.com.petsaude.regula_ai_backend.entity.Usuario;
import br.com.petsaude.regula_ai_backend.entity.enums.TipoUsuarioEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    Optional<Usuario> findByCpf(String cpf);

    Optional<Usuario> findByCrm(String crm);

    List<Usuario> findByTipo(TipoUsuarioEnum tipo);

    List<Usuario> findByAtivoTrue();

    boolean existsByCpf(String cpf);

    boolean existsByCrm(String crm);
}
