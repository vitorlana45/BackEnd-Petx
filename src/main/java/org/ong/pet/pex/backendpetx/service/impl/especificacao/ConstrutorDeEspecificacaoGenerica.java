// ConstrutorDeEspecificacaoDinamica.java
package org.ong.pet.pex.backendpetx.service.impl.especificacao;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

@Getter
@Setter
public class ConstrutorDeEspecificacaoGenerica<T> {

    private Specification<T> especificacao;

    public ConstrutorDeEspecificacaoGenerica() {
        this.especificacao = Specification.where(null);
    }

    public ConstrutorDeEspecificacaoGenerica<T> adicionarFiltroLikeIgnorandoMaiusculas(String nomeAtributo, String valor) {
        if (StringUtils.hasText(valor)) {
            especificacao = especificacao.and((raiz, consulta, criteriosConstrutor) ->
                    criteriosConstrutor.like(
                            criteriosConstrutor.lower(raiz.get(nomeAtributo)),
                            "%" + valor.toLowerCase() + "%"
                    )
            );
        }
        return this;
    }

    public <E extends Enum<E>> ConstrutorDeEspecificacaoGenerica<T> adicionarFiltroEnum(String nomeAtributo, E valor) {
        if (valor != null) {
            especificacao = especificacao.and((raiz, consulta, criteriosConstrutor) ->
                    criteriosConstrutor.equal(raiz.get(nomeAtributo), valor)
            );
        }
        return this;
    }

    public ConstrutorDeEspecificacaoGenerica<T> adicionarFiltroStringExata(String nomeAtributo, String valor) {
        if (StringUtils.hasText(valor)) {
            especificacao = especificacao.and((raiz, consulta, criteriosConstrutor) ->
                    criteriosConstrutor.equal(raiz.get(nomeAtributo), valor)
            );
        }
        return this;
    }


    public ConstrutorDeEspecificacaoGenerica<T> adicionarFiltroStringIniciais(String nomeAtributo, String valor) {

        if (StringUtils.hasText(valor)) {
            especificacao = especificacao.and((raiz, consulta, criteriosConstrutor) ->
                    criteriosConstrutor.like(
                            criteriosConstrutor.lower(raiz.get(nomeAtributo)),
                            valor.toLowerCase() + "%"
                    )
            );
        }
        return this;
    }

    public Specification<T> build() {
        return especificacao;
    }
}