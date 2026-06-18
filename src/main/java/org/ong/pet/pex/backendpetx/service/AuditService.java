package org.ong.pet.pex.backendpetx.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.ong.pet.pex.backendpetx.entity.Animal;
import org.ong.pet.pex.backendpetx.entity.revision.DiferencaCampo;
import org.ong.pet.pex.backendpetx.entity.revision.DiferencasUtil;
import org.ong.pet.pex.backendpetx.entity.revision.Revisao;
import org.ong.pet.pex.backendpetx.dto.RevisaoLinhaDTO;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditService {

  @PersistenceContext private EntityManager em;

  public Page<RevisaoLinhaDTO> listAnimalHistory(Long animalId, Pageable pageable) {
    AuditReader reader = AuditReaderFactory.get(em);

    var q = reader.createQuery()
      .forRevisionsOfEntity(Animal.class, false, true)   // select e, rev, revType
      .add(AuditEntity.id().eq(animalId))
      .addOrder(AuditEntity.revisionNumber().desc());

    @SuppressWarnings("unchecked")
    List<Object[]> raw = q.getResultList();

    List<RevisaoLinhaDTO> rows = new ArrayList<>(raw.size());
    for (Object[] r : raw) {
      Animal snapshot = (Animal) r[0];
      Revisao rev = (Revisao) r[1];
      RevisionType type = (RevisionType) r[2];
      rows.add(new RevisaoLinhaDTO(
        snapshot.getId(),
        snapshot.getNome() != null ? snapshot.getNome() : "(sem nome)",
        rev.getId(),
        Instant.ofEpochMilli(rev.getTimestamp()),
        rev.getUsername(),
        type
      ));
    }

    return page(rows, pageable);
  }

  public Animal snapshotAt(Long animalId, Number revision) {
    return AuditReaderFactory.get(em).find(Animal.class, animalId, revision);
  }

  public List<DiferencaCampo> diffAnimal(Long id, Number revA, Number revB) {
    Animal a = snapshotAt(id, revA);
    Animal b = snapshotAt(id, revB);
    // ignore campos técnicos
    var ignore = List.of("id", "arquivado", "arquivadoEm", "arquivadoPor", "motivoArquivamento");
    return DiferencasUtil.diff(a, b, ignore);
  }

  // helper para paginar lista em memória
  private <T> Page<T> page(List<T> all, Pageable pageable) {
    if (pageable == null) pageable = PageRequest.of(0, 20);
    int start = (int) pageable.getOffset();
    int end = Math.min(start + pageable.getPageSize(), all.size());
    List<T> content = start > all.size() ? List.of() : all.subList(start, end);
    return new PageImpl<>(content, pageable, all.size());
  }

  /** Lista as N últimas revisões de Animal (qualquer animal), mais recente primeiro. */
  public List<RevisaoLinhaDTO> listarRevisoesRecentes(int limite) {
    AuditReader leitor = AuditReaderFactory.get(em);

    AuditQuery q = leitor.createQuery()
            .forRevisionsOfEntity(Animal.class, false, true)   // retorna [entidade, Revisao, RevisionType]
            .addOrder(AuditEntity.revisionNumber().desc())
            .setMaxResults(Math.max(1, limite));

    @SuppressWarnings("unchecked")
    List<Object[]> rows = q.getResultList();

    List<RevisaoLinhaDTO> out = new ArrayList<>(rows.size());
    for (Object[] r : rows) {
      Animal snap     = (Animal) r[0];
      Revisao revisao = (Revisao) r[1];
      var tipo        = (RevisionType) r[2];

      out.add(new RevisaoLinhaDTO(
              snap.getId(),
              snap.getNome() != null ? snap.getNome() : "(sem nome)",
              revisao.getId(),
              java.time.Instant.ofEpochMilli(revisao.getTimestamp()),
              revisao.getUsername(),   // se você renomeou para getUsuario(), troque aqui
              tipo
      ));
    }
    return out;
  }


}
