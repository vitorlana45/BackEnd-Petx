package org.ong.pet.pex.backendpetx.repositories;

import org.ong.pet.pex.backendpetx.entities.Animal;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {


//    @Modifying
//    @Query(value = "DELETE FROM animal_conjunto_adocao WHERE animal_conjunto_id = :id OR animal_id = :id",
//            nativeQuery = true)
//    void deletarRelacoesConjunto(@Param("id") Long id);

    @Query("SELECT a FROM Animal a WHERE a.chipId = :chipId")
    Optional<Animal> findAnimalByChipId(String chipId);

    boolean existsAnimalByChipId(String chipId);

    @Query("SELECT a FROM Animal a WHERE a.chipId = :chipId")
    Animal getReferenceByChipId(String chipId);

    @Query(nativeQuery = true , value = """
            SELECT * FROM animal_doencas ad
            WHERE ad.animal_id = :id
            """)
    List<Animal> pegarTodasDoencasDoAnimal(long id);

    @EntityGraph(attributePaths = {"tutores", "doencas", "ong"})
    Optional<Animal> findAnimalById(Long id);

    @Query("SELECT a FROM Animal a WHERE a.chipId = :chipId AND a.estaVivo = :estaVivo")
    Optional<Animal> findAnimalByChipIdAndEstaVivo(String chipId, boolean estaVivo);


}
