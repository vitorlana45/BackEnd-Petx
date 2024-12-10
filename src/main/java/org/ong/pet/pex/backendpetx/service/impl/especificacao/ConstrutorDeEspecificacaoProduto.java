// ProdutoSpecificationBuilder.java
package org.ong.pet.pex.backendpetx.service.impl.especificacao;

import jakarta.persistence.criteria.MapJoin;
import jakarta.persistence.criteria.Predicate;
import lombok.Getter;
import lombok.Setter;
import org.ong.pet.pex.backendpetx.entities.Produto;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

@Getter
@Setter
public class ConstrutorDeEspecificacaoProduto extends ConstrutorDeEspecificacaoGenerica<Produto> {

    public ConstrutorDeEspecificacaoProduto adicionarFiltroPorAtributosEspecificos(String chave, String valor) {

        if (StringUtils.hasText(chave) && StringUtils.hasText(valor)) {
            Specification<Produto> especificacao = getEspecificacao().and((raiz, consulta, criteriosConstrutor) -> {
                MapJoin<Produto, String, String> atributosJoin = raiz.joinMap("atributosEspecificos");
                Predicate chavePredicado = criteriosConstrutor.equal(atributosJoin.key(), chave);
                Predicate valorPredicado = criteriosConstrutor.equal(atributosJoin.value(), valor);
                return criteriosConstrutor.and(chavePredicado, valorPredicado);
            });

            setEspecificacao(especificacao);
        }
        return this;
    }
}