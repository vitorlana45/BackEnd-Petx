package org.ong.pet.pex.backendpetx.service.mappers;

import lombok.AllArgsConstructor;
import org.ong.pet.pex.backendpetx.dto.request.InfoProdutoDTO;
import org.ong.pet.pex.backendpetx.dto.request.ProdutoDTO;
import org.ong.pet.pex.backendpetx.dto.response.ProdutoResposta;
import org.ong.pet.pex.backendpetx.entity.Estoque;
import org.ong.pet.pex.backendpetx.entity.Produto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ProdutoMapper {

    public Produto mapearParaEntidade(final ProdutoDTO dto, final Estoque estoque) {
        // Criar o produto com os campos básicos
       return Produto.builder()
                .nome(dto.nome())
                .descricao(dto.descricao())
                .quantidade(dto.quantidade())
                .unidadeDeMedida(dto.unidadeDeMedida())
//                .tipoProduto(dto.tipoProduto())
                .estoque(estoque)
                .atributosEspecificos(dto.atributosEspecificos().stream()
                        .collect(Collectors.toMap(InfoProdutoDTO::chave, InfoProdutoDTO::valor))
                )
                .build();
    }

    public ProdutoResposta mapearParaDto(final Produto entity) {
        // Converter atributos do produto para lista de InfoProdutoDTO
        List<InfoProdutoDTO> metaData = entity.getAtributosEspecificos().entrySet().stream()
                .map(entry -> new InfoProdutoDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return ProdutoResposta.builder()
                        .produtoId(entity.getId())
//                        .tipoProduto(entity.getTipoProduto())
                        .nome(entity.getNome())
                        .descricao(entity.getDescricao())
                        .quantidade(entity.getQuantidade())
                        .unidadeDeMedida(entity.getUnidadeDeMedida())
                        .atrubutosEspecificos(metaData)
                .build();
    }

    public List<ProdutoResposta> mapearListaProdutoParaDto(final List<Produto> produtos) {
        return produtos.stream()
                .map(this::mapearParaDto)
                .collect(Collectors.toList());
    }
}