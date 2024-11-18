package org.ong.pet.pex.backendpetx.repositories;

import org.ong.pet.pex.backendpetx.entities.Racao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RacaoRepository extends JpaRepository<Racao, Long> {
}