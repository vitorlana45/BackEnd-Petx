package org.ong.pet.pex.backendpetx.service.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
public class EstoqueException extends RuntimeException {

    private final Map<String, String> erros;
    private final HttpStatus status; // Adiciona o status

    public EstoqueException(String mensagem, HttpStatus status) {
        super(mensagem);
        this.erros = new HashMap<>();
        this.status = status;
    }

    public EstoqueException(String mensagem, Map<String, String> erros) {
        super(mensagem);
        this.erros = erros;
        this.status = HttpStatus.BAD_REQUEST; // Default para erros de validação
    }

    public static EstoqueException estoqueNaoEncontrado() {
        return new EstoqueException("Estoque nao encontrado", HttpStatus.NOT_FOUND);
    }

    public static EstoqueException estoqueNaoContemProdutosCadastrados() {
       return new EstoqueException("Estoque não contem produtos cadastrados", HttpStatus.NOT_FOUND);
    }

    public static EstoqueException produtoNaoEncontrado(String tipoProduto) {
        return new EstoqueException(String.format("Produto com o nome '%s'",tipoProduto), HttpStatus.NOT_FOUND);
    }
}
