package org.ong.pet.pex.backendpetx.repositories;

import org.ong.pet.pex.backendpetx.entities.Boletim;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface BoletimRepository extends JpaRepository<Boletim, Long> {

@Query(value = """
        SELECT * FROM boletins b
        WHERE (:numeroOcorrencia IS NULL OR b.numero_ocorrencia = :numeroOcorrencia)
        AND (:destino IS NULL OR b.destino = :destino)
    """,
        countQuery = """
        SELECT COUNT(*) FROM boletins b
        WHERE (:numeroOcorrencia IS NULL OR b.numero_ocorrencia = :numeroOcorrencia)
        AND (:destino IS NULL OR b.destino = :destino)
    """, nativeQuery = true)
    Page<Boletim> findAllBoletins(
            @Param("numeroOcorrencia") Long numeroOcorrencia,
            @Param("destino") String destino,
            Pageable pageable
    );
}
