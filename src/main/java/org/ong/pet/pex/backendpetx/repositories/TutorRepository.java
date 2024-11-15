package org.ong.pet.pex.backendpetx.repositories;

import org.ong.pet.pex.backendpetx.entities.Animal;
import org.ong.pet.pex.backendpetx.entities.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, Long> {

    @Modifying
    @Query("delete from Tutor t where :animal member of t.animais")
    void removeAnimalFromTutor(Animal animal);

    Optional<Tutor> findTutorByCpf(String cpf);

    boolean existsByCpf(String cpf);
}
