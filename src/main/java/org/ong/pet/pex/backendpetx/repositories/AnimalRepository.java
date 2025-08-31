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


//    @Query("""
//select a.id as id,
//       a.nome as nome,
//       a.raca as raca,
//       cast(a.especieEnum as string) as especieEnum,
//       cast(a.porteEnum as string) as porteEnum,
//       mf.objectKey as thumbKey
//from Animal a
//left join AnimalPhoto ap on ap.animal = a and ap.featured = true and ap.archived = false
//left join MediaFile mf on mf = ap.media
//where a.arquivado = false
//  and (:nome is null or lower(a.nome) like lower(concat(:nome, '%')))
//""")
//    Page<AnimalCardProjection> listarAnimaisComThumb(@Param("nome") String nome, Pageable pageable);

    // Estatística direta por origem garantindo que só conte animais com origem definida
    @Query("SELECT a.origemEnum as label, COUNT(a) as total FROM Animal a GROUP BY a.origemEnum")
    java.util.List<Object[]> countByOrigemAnimal();

}