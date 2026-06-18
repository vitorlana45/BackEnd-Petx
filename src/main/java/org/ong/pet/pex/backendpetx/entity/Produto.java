package org.ong.pet.pex.backendpetx.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.ong.pet.pex.backendpetx.enums.TipoProduto;
import org.ong.pet.pex.backendpetx.enums.UnidadeDeMedidaEnum;

import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "produto")
public class Produto extends EntidadeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "nome")
    private String nome;

    @Column(nullable = false, name = "descricao")
    private String descricao;

    @Column(nullable = false, name = "quantidade")
    private Double quantidade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "unidade_de_medida")
    private UnidadeDeMedidaEnum unidadeDeMedida;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_produto")
    private TipoProduto tipoProduto;

    @Column(name = "preco", precision = 10, scale = 2)
    private java.math.BigDecimal preco;

    // Atributos específicos armazenados como chave-valor
    @ElementCollection
    @CollectionTable(name = "produto_atributos", joinColumns = @JoinColumn(name = "produto_id"))
    @MapKeyColumn(name = "atributo_chave")
    @Column(name = "atributo_valor")
    private Map<String, String> atributosEspecificos;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_estoque_id", nullable = false)
    private CategoriaEstoque categoriaEstoque;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "estoque_id", nullable = false)
    private Estoque estoque;

    // Método de conveniência para obter o nome da categoria
    public String getCategoria() {
        return categoriaEstoque != null ? categoriaEstoque.getNome() : null;
    }

    public void adicionarAtributo(String chave, String valor) {
        atributosEspecificos.put(chave, valor);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return Objects.equals(id, produto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}