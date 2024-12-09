package org.ong.pet.pex.backendpetx.service.impl.serviceUtils;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class ConstrutorDeEspecificacaoDinamica<T> {

    // A variável que armazenará a especificação construída ao longo do tempo
    private Specification<T> especificacao;

    // Construtor inicial, começa com uma especificação vazia
    public ConstrutorDeEspecificacaoDinamica() {
        this.especificacao = Specification.where(null);  // Inicia a especificação com 'null', sem filtro
    }

    // Metodo para adicionar um filtro "like" ignorando maiúsculas/minúsculas em uma string
    public ConstrutorDeEspecificacaoDinamica<T> adicionarFiltroLikeIgnorandoMaiusculas(
            String nomeAtributo,   // Nome do atributo (coluna) a ser filtrado
            String valor           // Valor para buscar dentro do atributo
    ) {
        // Verifica se o valor não é nulo e não é uma string vazia
        if (StringUtils.hasText(valor)) {
            // Adiciona o filtro à especificação
            especificacao = especificacao.and((raiz, consulta, criteriosConstrutor) ->
                    criteriosConstrutor.like(
                            criteriosConstrutor.lower(raiz.get(nomeAtributo)),  // Converte o valor do atributo para minúsculo
                            "%" + valor.toLowerCase() + "%"  // Adiciona os percentuais para busca por "contém"
                    )
            );
        }
        // Retorna o próprio objeto para permitir encadeamento de métodos
        return this;
    }

    // Metodo para adicionar um filtro de comparação exata para atributos do tipo enum
    public <E extends Enum<E>> ConstrutorDeEspecificacaoDinamica<T> adicionarFiltroEnum(
            String nomeAtributo,  // Nome do atributo (coluna) do tipo Enum
            E valor               // Valor do Enum para comparar
    ) {
        // Verifica se o valor do Enum não é nulo
        if (valor != null) {
            // Adiciona o filtro à especificação
            especificacao = especificacao.and((raiz, consulta, criteriosConstrutor) ->
                    criteriosConstrutor.equal(raiz.get(nomeAtributo), valor)  // Compara o atributo com o valor exato
            );
        }
        // Retorna o próprio objeto para permitir encadeamento de métodos
        return this;
    }

    // Metodo para adicionar um filtro de comparação exata para atributos do tipo String
    public ConstrutorDeEspecificacaoDinamica<T> adicionarFiltroStringExata(
            String nomeAtributo,   // Nome do atributo (coluna) a ser filtrado
            String valor           // Valor exato a ser comparado com o atributo
    ) {
        // Verifica se o valor não é nulo e não é uma string vazia
        if (StringUtils.hasText(valor)) {
            // Adiciona o filtro à especificação
            especificacao = especificacao.and((raiz, consulta, criteriosConstrutor) ->
                    criteriosConstrutor.equal(raiz.get(nomeAtributo), valor)  // Compara o atributo com o valor exato
            );
        }
        // Retorna o próprio objeto para permitir encadeamento de métodos
        return this;
    }


    // Metodo final para obter a especificação construída
    public Specification<T> build() {
        return especificacao;  // Retorna a especificação construída com todos os filtros aplicados
    }
}
