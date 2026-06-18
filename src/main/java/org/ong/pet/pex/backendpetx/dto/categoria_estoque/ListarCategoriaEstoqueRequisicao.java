package org.ong.pet.pex.backendpetx.dto.categoria_estoque;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListCategoriaEstoqueRequest {

    private String nome;
    private Long estoqueId;
    private int page = 0;
    private int size = 10;
    private String sort = "nome";
    private String direction = "asc";
}
