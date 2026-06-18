package org.ong.pet.pex.backendpetx.service.impl;

import lombok.RequiredArgsConstructor;
import org.ong.pet.pex.backendpetx.entity.LogAtividade;
import org.ong.pet.pex.backendpetx.repository.LogAtividadeRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LogAtividadeService {

  private final LogAtividadeRepository repo;

  @Transactional
  public void log(String entidade, String acao, Long entidadeId, String mensagem, String usuario, Map<String,Object> meta) {
    var log = LogAtividade.builder()
        .ts(OffsetDateTime.now())
        .usuario(usuario)
        .acao(acao)
        .entidade(entidade)
        .entidadeId(entidadeId)
        .mensagem(mensagem)
        .meta(meta)
        .build();
    repo.save(log);
  }

  public Page<LogAtividade> listarRecentes(int page, int size) {
    return repo.findRecent(PageRequest.of(Math.max(page,0), Math.min(100, size)));
  }
}
