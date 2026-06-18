package org.ong.pet.pex.backendpetx.repositories;

import org.ong.pet.pex.backendpetx.entities.CategoriaEstoque;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaEstoqueRepository extends JpaRepository<CategoriaEstoque, Long> {

    Optional<CategoriaEstoque> findByNomeAndEstoqueId(String nome, Long estoqueId);

    List<CategoriaEstoque> findByEstoqueId(Long estoqueId);

    @Query(
            value = """
    SELECT * FROM categoria_estoque_tb c
    WHERE (:nome IS NULL OR c.nome ILIKE CONCAT('%', :nome, '%'))
      AND (:estoqueId IS NULL OR c.estoque_id = :estoqueId)
    ORDER BY c.nome
  """,
            countQuery = """
    SELECT count(*) FROM categoria_estoque_tb c
    WHERE (:nome IS NULL OR c.nome ILIKE CONCAT('%', :nome, '%'))
      AND (:estoqueId IS NULL OR c.estoque_id = :estoqueId)
  """,
            nativeQuery = true
    )
    Page<CategoriaEstoque> findByFilters(@Param("nome") String nome,
                                               @Param("estoqueId") Long estoqueId,
                                               Pageable pageable);

    boolean existsByNomeAndEstoqueIdAndIdNot(String nome, Long estoqueId, Long id);
}
