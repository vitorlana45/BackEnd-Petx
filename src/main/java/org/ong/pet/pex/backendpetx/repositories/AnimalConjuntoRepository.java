package org.ong.pet.pex.backendpetx.repositories;

import org.ong.pet.pex.backendpetx.entities.Animal;
import org.ong.pet.pex.backendpetx.entities.AnimalConjunto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AnimalConjuntoRepository  extends JpaRepository<AnimalConjunto, Long> {

    @Query("select a from AnimalConjunto a where (a.animalPrincipal.id = ?1) or (a.animalRelacionamento.id = ?1)")
    List<AnimalConjunto> findByAnimalPrincipalIdOrAnimalRelacionamentoId(Long animalPrincipalId);

    @Query("select a from AnimalConjunto a where (a.animalPrincipal.chipId = ?1) or (a.animalRelacionamento.chipId = ?1)")
    List<AnimalConjunto> findByAnimalPrincipalIdOrAnimalRelacionamentoChipId(String animalPrincipalChipId);

    Optional<AnimalConjunto> findByAnimalRelacionamentoId(Long animalRelacionamentoId);


    void deleteAnimalConjuntoByAnimalConjuntoId(Long id);

    List<AnimalConjunto> findByAnimalPrincipalId(Long animalPrincipalId);
}
