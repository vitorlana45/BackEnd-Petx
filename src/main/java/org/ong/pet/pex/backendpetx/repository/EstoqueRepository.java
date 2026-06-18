package org.ong.pet.pex.backendpetx.repository;

import org.ong.pet.pex.backendpetx.entity.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {

    /**
     * Busca estoques por nome que contém o texto ignorando case
     */
    List<Estoque> findByNomeContainingIgnoreCase(String nome);
}
