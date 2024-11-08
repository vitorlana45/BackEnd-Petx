package org.ong.pet.pex.backendpetx.repositories;

import org.ong.pet.pex.backendpetx.entities.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContatoRepository extends JpaRepository<Contact, Long> {
}
