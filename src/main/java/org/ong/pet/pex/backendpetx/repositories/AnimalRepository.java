package org.ong.pet.pex.backendpetx.repositories;

import org.ong.pet.pex.backendpetx.dto.response.AnimalGenericoResposta;
import org.ong.pet.pex.backendpetx.entities.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

    @Query("SELECT a FROM Animal a WHERE a.nome = :nome")
    Animal findAnimalByNome(String nome);

    @Modifying
    @Query(value = "DELETE FROM animal_conjunto_adocao WHERE animal_conjunto_id = :id OR animal_id = :id",
            nativeQuery = true)
    void deletarRelacoesConjunto(@Param("id") Long id);

    @Query("SELECT a FROM Animal a WHERE a.chipId = :chipId")
    Animal findAnimalByChipId(String chipId);

    boolean existsAnimalByChipId(String chipId);

}
