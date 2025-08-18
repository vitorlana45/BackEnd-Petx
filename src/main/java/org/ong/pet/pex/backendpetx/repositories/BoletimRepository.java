package org.ong.pet.pex.backendpetx.repositories;

import org.ong.pet.pex.backendpetx.entities.Boletim;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.ong.pet.pex.backendpetx.enums.Destino;
import java.time.LocalDateTime;
import java.util.List;


public interface BoletimRepository extends JpaRepository<Boletim, Long> {

    @Query("SELECT b FROM Boletim b WHERE (:numeroOcorrencia IS NULL OR b.numeroOcorrencia = :numeroOcorrencia) " +
        "AND (:destino IS NULL OR b.destino = :destino)")
    Page<Boletim> findAllBoletins(@Param("numeroOcorrencia") Long numeroOcorrencia,
                  @Param("destino") Destino destino,
                  Pageable pageable);

    // ===== Estatísticas =====
    @Query("SELECT a.origemEnum as label, COUNT(b) as total FROM Boletim b LEFT JOIN b.animal a GROUP BY a.origemEnum")
    List<Object[]> countByOrigem();

    @Query("SELECT b.destino as label, COUNT(b) as total FROM Boletim b GROUP BY b.destino")
    List<Object[]> countByDestino();

    @Query("SELECT a.especieEnum as label, COUNT(b) as total FROM Boletim b LEFT JOIN b.animal a GROUP BY a.especieEnum")
    List<Object[]> countByEspecie();

    @Query("SELECT FUNCTION('date_trunc','month', b.dataAtendimento) as periodo, COUNT(b) as total FROM Boletim b WHERE b.dataAtendimento >= :inicio GROUP BY FUNCTION('date_trunc','month', b.dataAtendimento) ORDER BY periodo")
    List<Object[]> countByMesDesde(@Param("inicio") LocalDateTime inicio);
}
