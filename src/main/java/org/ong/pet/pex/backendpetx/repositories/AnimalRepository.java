package org.ong.pet.pex.backendpetx.repositories;

import org.ong.pet.pex.backendpetx.entities.Animal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long>, JpaSpecificationExecutor<Animal> {

    @Query("SELECT a FROM Animal a WHERE a.chipId = :chipId")
    Optional<Animal> findAnimalByChipId(String chipId);

    boolean existsAnimalByChipId(String chipId);

    @EntityGraph(attributePaths = {"tutores", "doencas", "ong"})
    Optional<Animal> findAnimalById(Long id);


    @Query(value = """
    SELECT * FROM animal_tb a
    WHERE
        (
            (:raca IS NULL OR LOWER(a.raca) LIKE LOWER(CONCAT('%', :raca, '%')))
            AND
            (:nome IS NULL OR LOWER(a.nome) LIKE LOWER(CONCAT(:nome, '%')))
        )
    AND (:especie IS NULL OR a.especie = :especie)
    AND (:porte IS NULL OR a.porte = :porte)
    AND (:status IS NULL OR a.status = :status)
    AND (:comportamento IS NULL OR a.comportamento = :comportamento)
    AND (:maturidade IS NULL OR a.maturidade = :maturidade)
    AND (:origem IS NULL OR a.origem = :origem)
    AND (:sexo IS NULL OR a.sexo = :sexo)
""",
            countQuery = """
    SELECT COUNT(*) FROM animal_tb a
    WHERE
        (
            (:raca IS NULL OR LOWER(a.raca) LIKE LOWER(CONCAT('%', :raca, '%')))
            AND
            (:nome IS NULL OR LOWER(a.nome) LIKE LOWER(CONCAT(:nome, '%')))
        )
    AND (:especie IS NULL OR a.especie = :especie)
    AND (:porte IS NULL OR a.porte = :porte)
    AND (:status IS NULL OR a.status = :status)
    AND (:comportamento IS NULL OR a.comportamento = :comportamento)
    AND (:maturidade IS NULL OR a.maturidade = :maturidade)
    AND (:origem IS NULL OR a.origem = :origem)
    AND (:sexo IS NULL OR a.sexo = :sexo)
""",
            nativeQuery = true)
    Page<Animal> findAllPorFiltro(
            @Param("nome") String nome,
            @Param("raca") String raca,
            @Param("especie") String especie,
            @Param("porte") String porte,
            @Param("status") String status,
            @Param("comportamento") String comportamento,
            @Param("maturidade") String maturidade,
            @Param("origem") String origem,
            @Param("sexo") String sexo,
            Pageable pageable);

}
