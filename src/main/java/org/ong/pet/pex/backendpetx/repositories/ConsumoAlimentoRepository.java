package org.ong.pet.pex.backendpetx.repositories;

import org.ong.pet.pex.backendpetx.entities.ConsumoAlimento;
import org.ong.pet.pex.backendpetx.enums.PorteEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConsumoAlimentoRepository extends JpaRepository<ConsumoAlimento, Long> {

    Optional<ConsumoAlimento> findByPorte(PorteEnum porte);
}
