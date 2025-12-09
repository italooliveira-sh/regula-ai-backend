package br.com.petsaude.regula_ai_backend.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO de entrada para criação de item de solicitação.
 * O front-end envia apenas o ID do procedimento e a quantidade.
 * O back-end busca os dados do catálogo e aplica o padrão Snapshot.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemSolicitacaoDTO {

    @NotNull(message = "O ID do procedimento é obrigatório")
    private UUID procedimentoId;

    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade deve ser pelo menos 1")
    private Integer quantidade;

}
