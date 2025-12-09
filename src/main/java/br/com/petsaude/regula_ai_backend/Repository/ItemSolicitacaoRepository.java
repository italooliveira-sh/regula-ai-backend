package br.com.petsaude.regula_ai_backend.Repository;

import br.com.petsaude.regula_ai_backend.entity.ItemSolicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItemSolicitacaoRepository extends JpaRepository<ItemSolicitacao, UUID> {

    List<ItemSolicitacao> findBySolicitacaoId(UUID solicitacaoId);

    List<ItemSolicitacao> findByCode(String code);

    List<ItemSolicitacao> findByCodeSystem(String codeSystem);
}
