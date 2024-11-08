package org.ong.pet.pex.backendpetx.repositories;

import org.ong.pet.pex.backendpetx.entities.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, Long> {
    Tutor findByEmail(String email);
}
