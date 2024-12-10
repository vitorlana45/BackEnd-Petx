package org.ong.pet.pex.backendpetx.repositories;

import org.ong.pet.pex.backendpetx.entities.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EstoqueRepository extends JpaRepository<Estoque, Long>, JpaSpecificationExecutor<Estoque> {


    Optional<Estoque> findEstoqueByOngId(Long ong);

    @Query("SELECT e FROM Estoque e JOIN FETCH e.produtos WHERE e.id = :id")
    Optional<Estoque> findByIdWithProdutos(@Param("id") Long id);

}
