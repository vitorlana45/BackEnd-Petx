package org.ong.pet.pex.backendpetx.service.mappers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import org.ong.pet.pex.backendpetx.dto.request.InfoProdutoDTO;
import org.ong.pet.pex.backendpetx.dto.request.ProdutoDTO;
import org.ong.pet.pex.backendpetx.entities.Estoque;
import org.ong.pet.pex.backendpetx.entities.Produto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ProdutoMapper {
    private final Gson gson = new Gson();

    public Produto mapearParaEntidade(final ProdutoDTO dto, final Estoque estoque){
        var json = gson.toJson(dto.metaData());

        return Produto.builder()
                .nome(dto.nome())
                .descricao(dto.descricao())
                .quantidade(dto.quantidade())
                .unidadeDeMedida(dto.unidadeDeMedida())
                .tipoProduto(dto.tipoProduto())
                .estoque(estoque)
                .metaData(json)
                .build();
    }

    public ProdutoDTO mapearParaDto(final Produto entity){
        var listType = new TypeToken<List<InfoProdutoDTO>>() {}.getType();
        List<InfoProdutoDTO> metaData = gson.fromJson(entity.getMetaData(), listType);
        return ProdutoDTO.builder()
                .nome(entity.getNome())
                .descricao(entity.getDescricao())
                .quantidade(entity.getQuantidade())
                .unidadeDeMedida(entity.getUnidadeDeMedida())
                .tipoProduto(entity.getTipoProduto())
                .metaData(metaData)
                .build();
    }

    public List<ProdutoDTO> meparParaDto(final List<Produto> produtos){
        return produtos.stream().map(this::mapearParaDto).collect(Collectors.toList());
    }
}
