package org.ong.pet.pex.backendpetx.repositories;

import org.ong.pet.pex.backendpetx.entities.Receita;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Long> {

    @Query(value = """
    SELECT * FROM receitas_tb r
        WHERE
            (:categoria IS NULL OR r.categoria = :categoria)
            AND (:valor IS NULL OR r.valor = :valor)
            AND (:dataRecebimento IS NULL OR r.data_recebimento = :dataRecebimento)
            AND (:formaPagamento IS NULL OR r.forma_pagamento = :formaPagamento)
            AND (:descricao IS NULL OR LOWER(r.descricao) LIKE LOWER(CONCAT('%', :descricao, '%')))
""",
            countQuery = """
    SELECT COUNT(*) FROM receitas_tb r
        WHERE
            (:categoria IS NULL OR r.categoria = :categoria)
            AND (:valor IS NULL OR r.valor = :valor)
            AND (:dataRecebimento IS NULL OR r.data_recebimento = :dataRecebimento)
            AND (:formaPagamento IS NULL OR r.forma_pagamento = :formaPagamento)
            AND (:descricao IS NULL OR LOWER(r.descricao) LIKE LOWER(CONCAT('%', :descricao, '%')))
""", nativeQuery = true)
    Page<Receita> findAllReceitas(
            @Param("descricao") String descricao,
            @Param("valor") BigDecimal valor,
            @Param("dataRecebimento") LocalDate dataRecebimento,
            @Param("categoria") String categoria,
            @Param("formaPagamento") String formaPagamento,
            Pageable pageable);
}

