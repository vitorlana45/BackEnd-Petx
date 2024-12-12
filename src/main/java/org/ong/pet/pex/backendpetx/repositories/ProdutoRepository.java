package org.ong.pet.pex.backendpetx.repositories;

import org.ong.pet.pex.backendpetx.entities.Produto;
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
        AND (:tipoProduto IS NULL OR p.tipo_produto = :tipoProduto)
        AND (:quantidade IS NULL OR p.quantidade = :quantidade)
        AND (:unidadeDeMedida IS NULL OR p.unidade_de_medida = :unidadeDeMedida)
    """,
        countQuery = """
         SELECT * FROM Produto p
        WHERE (:nome IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%')))
        AND (:tipoProduto IS NULL OR p.tipo_produto = :tipoProduto)
        AND (:quantidade IS NULL OR p.quantidade = :quantidade)
        AND (:unidadeDeMedida IS NULL OR p.unidade_de_medida = :unidadeDeMedida)
    """,nativeQuery = true)
Page<Produto> findAllProdutos(
        @Param("tipoProduto") String tipoProduto,
        @Param("nome") String nome,
        @Param("quantidade") Double quantidade,
        @Param("unidadeDeMedida") String unidadeDeMedida,
        Pageable pageable);

}