package org.ong.pet.pex.backendpetx.repository;

import org.ong.pet.pex.backendpetx.entity.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ProdutoRepository extends JpaRepository<Produto, Long> {
//se eu tentar fazer uma comparação de nome usando a clausula AND não suporta a transformação de texto como UPPER() e LOWER()
@Query(value = """
        SELECT * FROM Produto p
        WHERE (:nome IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%')))
        AND (:tipoProduto IS NULL)
        AND (:quantidade IS NULL OR p.quantidade = :quantidade)
        AND (:unidadeDeMedida IS NULL OR p.unidade_de_medida = :unidadeDeMedida)
    """,
        countQuery = """
         SELECT * FROM Produto p
        WHERE (:nome IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%')))
        AND (:tipoProduto IS NULL)
        AND (:quantidade IS NULL OR p.quantidade = :quantidade)
        AND (:unidadeDeMedida IS NULL OR p.unidade_de_medida = :unidadeDeMedida)
    """,nativeQuery = true)
Page<Produto> findAllProdutos(
        @Param("tipoProduto") String tipoProduto,
        @Param("nome") String nome,
        @Param("quantidade") Double quantidade,
        @Param("unidadeDeMedida") String unidadeDeMedida,
        Pageable pageable);

    /**
     * Conta produtos por estoque
     */
    @Query("SELECT COUNT(p) FROM Produto p WHERE p.estoque.id = :estoqueId")
    long countByEstoqueId(@Param("estoqueId") Long estoqueId);

    /**
     * Busca pelo nome que contém o texto no estoque específico
     */
    @Query("SELECT p FROM Produto p WHERE p.estoque.id = :estoqueId AND (:nome IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%')))" )
    Page<Produto> findByEstoqueIdAndNomeContaining(@Param("estoqueId") Long estoqueId, @Param("nome") String nome, Pageable pageable);
}