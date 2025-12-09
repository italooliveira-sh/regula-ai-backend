package br.com.petsaude.regula_ai_backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO resumido para representação de Unidade em responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnidadeResumoDTO {

    private UUID id;
    private String nome;
    private String cnes;
    private String tipoUnidade;
}
