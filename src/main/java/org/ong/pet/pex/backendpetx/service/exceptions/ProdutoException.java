package org.ong.pet.pex.backendpetx.service.exceptions;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class ProdutoException extends RuntimeException {

  private final Map<String, String> erros;
  private final HttpStatus status; // Adiciona o status

  public ProdutoException(String mensagem, HttpStatus status) {
    super(mensagem);
    this.erros = new HashMap<>();
    this.status = status;
  }

  public ProdutoException(String mensagem, Map<String, String> erros) {
    super(mensagem);
    this.erros = erros;
    this.status = HttpStatus.BAD_REQUEST; // Default para erros de validação
  }

  public static ProdutoException produtoNaoEncontrado(String id) {
    return new ProdutoException(String.format("produto com o identificador '%s' não encontrado", id), HttpStatus.NOT_FOUND);
  }

  public static ProdutoException campoObrigatorio (String id) {
    return new ProdutoException(String.format("produto com o identificador '%s' não encontrado", id), HttpStatus.NOT_FOUND);
  }
}
