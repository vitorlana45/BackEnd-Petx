package org.ong.pet.pex.backendpetx.dto.categoria_estoque;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtualizarCategoriaEstoqueResposta {

    private Long id;
    private String nome;
    private String descricao;
    private Long estoqueId;
    private String estoqueNome;
    private LocalDateTime dataAtualizacao;
    private String mensagem;
}
