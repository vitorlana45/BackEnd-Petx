package org.ong.pet.pex.backendpetx.repositories;

import org.ong.pet.pex.backendpetx.entities.Animal;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

    @Query("SELECT a FROM Animal a WHERE a.obituario.status = :status")
    List<Animal> procurarTodosAnimaisComVivosOuMortos(boolean status);
}
