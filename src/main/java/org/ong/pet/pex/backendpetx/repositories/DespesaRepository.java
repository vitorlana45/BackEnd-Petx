package org.ong.pet.pex.backendpetx.repositories;

import org.ong.pet.pex.backendpetx.entities.Despesa;
import org.ong.pet.pex.backendpetx.enums.CategoriaDespesaEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DespesaRepository extends JpaRepository<Despesa, Long> {

    @Query("SELECT d FROM Despesa d WHERE d.categoria = :filtro")
    List<Despesa> listarDespesaFiltrada(CategoriaDespesaEnum filtro);
}
