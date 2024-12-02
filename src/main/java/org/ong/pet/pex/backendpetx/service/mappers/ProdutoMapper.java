package org.ong.pet.pex.backendpetx.service.mappers;

import lombok.AllArgsConstructor;
import org.ong.pet.pex.backendpetx.dto.request.InfoProdutoDTO;
import org.ong.pet.pex.backendpetx.dto.request.ProdutoDTO;
import org.ong.pet.pex.backendpetx.dto.response.ProdutoDTOResposta;
import org.ong.pet.pex.backendpetx.entities.Estoque;
import org.ong.pet.pex.backendpetx.entities.Produto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ProdutoMapper {

    public Produto mapearParaEntidade(final ProdutoDTO dto, final Estoque estoque) {
        // Criar o produto com os campos básicos
        Produto produto = Produto.builder()
                .nome(dto.nome())
                .descricao(dto.descricao())
                .quantidade(dto.quantidade())
                .unidadeDeMedida(dto.unidadeDeMedida())
                .tipoProduto(dto.tipoProduto())
                .estoque(estoque)
                .atributos(dto.atributosDinamicos().stream()
                        .collect(Collectors.toMap(InfoProdutoDTO::chave, InfoProdutoDTO::valor))
                )
                .build();


        // Adicionar metadados como atributos dinâmicos
        if (dto.atributosDinamicos() != null) {
            dto.atributosDinamicos().forEach(info ->
                    produto.adicionarAtributo(info.chave(), info.valor())
            );
        }

        return produto;
    }

    public ProdutoDTOResposta mapearParaDto(final Produto entity) {
        // Converter atributos do produto para lista de InfoProdutoDTO
        List<InfoProdutoDTO> metaData = entity.getAtributos().entrySet().stream()
                .map(entry -> new InfoProdutoDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return ProdutoDTOResposta.builder()
                .produtoId(entity.getId())
                .produto(ProdutoDTO.builder()
                        .tipoProduto(entity.getTipoProduto())
                        .nome(entity.getNome())
                        .descricao(entity.getDescricao())
                        .quantidade(entity.getQuantidade())
                        .unidadeDeMedida(entity.getUnidadeDeMedida())
                        .atributosDinamicos(metaData)
                        .build())
                .build();
    }

    public List<ProdutoDTOResposta> mapearListaProdutoParaDto(final List<Produto> produtos) {
        return produtos.stream()
                .map(this::mapearParaDto)
                .collect(Collectors.toList());
    }
}