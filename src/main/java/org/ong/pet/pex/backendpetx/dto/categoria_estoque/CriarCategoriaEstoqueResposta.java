package org.ong.pet.pex.backendpetx.dto.categoria_estoque;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CriarCategoriaEstoqueResposta {

    private Long id;
    private String nome;
    private String descricao;
    private Long estoqueId;
    private String estoqueNome;
    private LocalDateTime dataCriacao;
    private String mensagem;
}
