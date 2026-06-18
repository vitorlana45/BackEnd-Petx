package org.ong.pet.pex.backendpetx.repositories;

import org.ong.pet.pex.backendpetx.entities.Boletim;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.ong.pet.pex.backendpetx.enums.Destino;
import org.ong.pet.pex.backendpetx.enums.OrigemAnimalEnum;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BoletimRepository extends JpaRepository<Boletim, Long>, JpaSpecificationExecutor<Boletim> {

    Optional<Boletim> findByNumeroOcorrencia(Long numeroOcorrencia);

    boolean existsByNumeroOcorrencia(Long numeroOcorrencia);

    boolean existsByNumeroOcorrenciaAndOng_Id(Long numeroOcorrencia, Long ongId);

    Optional<Boletim> findByNumeroOcorrenciaAndOng_Id(Long numeroOcorrencia, Long ongId);

    @Query("SELECT MAX(b.numeroOcorrencia) FROM Boletim b WHERE b.numeroOcorrencia BETWEEN :inicio AND :fim")
    Long findMaxNumeroOcorrenciaInRange(@Param("inicio") Long inicio, @Param("fim") Long fim);

    @Query("SELECT b FROM Boletim b WHERE (:numeroOcorrencia IS NULL OR b.numeroOcorrencia = :numeroOcorrencia) " +
        "AND (:destino IS NULL OR b.destino = :destino)")
    Page<Boletim> findAllBoletins(@Param("numeroOcorrencia") Long numeroOcorrencia,
                  @Param("destino") Destino destino,
                  Pageable pageable);

    @Query("SELECT b FROM Boletim b " +
            "WHERE b.ong.id = :ongId " +
            "AND (:inicio IS NULL OR b.dataAtendimento >= :inicio) " +
            "AND (:fim IS NULL OR b.dataAtendimento <= :fim) " +
            "AND (:origem IS NULL OR b.origem = :origem) " +
            "AND (:destino IS NULL OR b.destino = :destino) " +
            "ORDER BY b.dataAtendimento DESC")
    Page<Boletim> searchForVinculo(@Param("ongId") Long ongId,
                                  @Param("inicio") LocalDateTime inicio,
                                  @Param("fim") LocalDateTime fim,
                                  @Param("origem") OrigemAnimalEnum origem,
                                  @Param("destino") Destino destino,
                                  Pageable pageable);

    // ===== Estatísticas =====
    @Query("SELECT a.origemEnum as label, COUNT(a) as total FROM Boletim b LEFT JOIN b.animais a GROUP BY a.origemEnum")
    List<Object[]> countByOrigem();

    @Query("SELECT b.destino as label, COUNT(b) as total FROM Boletim b GROUP BY b.destino")
    List<Object[]> countByDestino();

    @Query("SELECT a.especieEnum as label, COUNT(a) as total FROM Boletim b LEFT JOIN b.animais a GROUP BY a.especieEnum")
    List<Object[]> countByEspecie();

    @Query("SELECT FUNCTION('date_trunc','month', b.dataAtendimento) as periodo, COUNT(b) as total FROM Boletim b WHERE b.dataAtendimento >= :inicio GROUP BY FUNCTION('date_trunc','month', b.dataAtendimento) ORDER BY periodo")
    List<Object[]> countByMesDesde(@Param("inicio") LocalDateTime inicio);
}
