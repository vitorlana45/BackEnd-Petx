package org.ong.pet.pex.backendpetx.repositories;

import org.ong.pet.pex.backendpetx.entities.Despesa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;


public interface DespesaRepository extends JpaRepository<Despesa, Long> {

    @Query(value = """
    SELECT * FROM despesas_tb d
        WHERE
            (
                (:categoria IS NULL OR d.categoria_despesa = :categoria)
            )
            AND (:valor IS NULL OR d.valor = :valor)
            AND (:prevista IS NULL OR d.data_prevista_pagamento = :prevista)
            AND (:formaPagamento IS NULL OR d.forma_pagamento = :formaPagamento)
            AND (:dataPagamento IS NULL OR d.data_pagamento = :dataPagamento)
            AND (:status IS NULL OR d.status_despesa = :status)
            AND (:descricao IS NULL OR LOWER(d.descricao) LIKE LOWER(CONCAT('%', :descricao, '%')))
""",
            countQuery = """
    SELECT COUNT(*) FROM despesas_tb d
        WHERE
            (
                (:categoria IS NULL OR d.categoria_despesa = :categoria)
            )
            AND (:valor IS NULL OR d.valor = :valor)
            AND (:prevista IS NULL OR d.data_prevista_pagamento = :prevista)
            AND (:formaPagamento IS NULL OR d.forma_pagamento = :formaPagamento)
            AND (:dataPagamento IS NULL OR d.data_pagamento = :dataPagamento)
            AND (:status IS NULL OR d.status_despesa = :status)
            AND (:descricao IS NULL OR LOWER(d.descricao) LIKE LOWER(CONCAT('%', :descricao, '%')))
""", nativeQuery = true)
    Page<Despesa> findAllDespesa(
            @Param("descricao") String descricao,
            @Param("valor") BigDecimal valor,
            @Param("prevista") LocalDate prevista,
            @Param("categoria") String categoria,
            @Param("formaPagamento") String formaPagamento,
            @Param("dataPagamento") LocalDate dataPagamento,
            @Param("status") String status,
            Pageable pageable);
}