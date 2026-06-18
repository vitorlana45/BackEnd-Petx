package org.ong.pet.pex.backendpetx.repository;

import org.ong.pet.pex.backendpetx.entity.Ong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OngRepository extends JpaRepository<Ong, Long> {
}
