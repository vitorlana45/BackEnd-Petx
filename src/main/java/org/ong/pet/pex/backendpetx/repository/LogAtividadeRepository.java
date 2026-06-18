package org.ong.pet.pex.backendpetx.repository;

import org.ong.pet.pex.backendpetx.entity.LogAtividade;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface LogAtividadeRepository extends JpaRepository<LogAtividade, Long> {

  @Query("select l from LogAtividade l order by l.ts desc")
  Page<LogAtividade> findRecent(Pageable pageable);
}
