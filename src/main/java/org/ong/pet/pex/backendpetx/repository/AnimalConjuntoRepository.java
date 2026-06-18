package org.ong.pet.pex.backendpetx.repositories;

import org.ong.pet.pex.backendpetx.entities.AnimalConjunto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AnimalConjuntoRepository  extends JpaRepository<AnimalConjunto, Long> {

    @Query("select a from AnimalConjunto a where (a.animalPrincipal.id = ?1) or (a.animalRelacionamento.id = ?1)")
    List<AnimalConjunto> findByAnimalPrincipalIdOrAnimalRelacionamentoId(Long animalPrincipalId);

    Optional<AnimalConjunto> findByAnimalRelacionamentoId(Long animalRelacionamentoId);

    void deleteAnimalConjuntoByAnimalConjuntoId(Long id);

    List<AnimalConjunto> findByAnimalPrincipalId(Long animalPrincipalId);
}
