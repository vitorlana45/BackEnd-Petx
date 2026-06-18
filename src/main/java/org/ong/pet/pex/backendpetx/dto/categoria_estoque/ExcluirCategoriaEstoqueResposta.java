package org.ong.pet.pex.backendpetx.dto.categoria_estoque;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteCategoriaEstoqueResponse {

    private Long id;
    private String nome;
    private String mensagem;
    private boolean sucesso;
}
