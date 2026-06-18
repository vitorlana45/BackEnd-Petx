package org.ong.pet.pex.backendpetx.repository;

import org.ong.pet.pex.backendpetx.entity.Obito;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObitoRepository extends JpaRepository<Obito, Long> {

    Obito findByAnimalId(Long animalId);

}
