package org.ong.pet.pex.backendpetx.entities;

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
    @Column(nullable = false, name = "tipo_produto")
    private TipoProduto tipoProduto;

    // Atributos específicos armazenados como chave-valor
    @ElementCollection
    @CollectionTable(name = "produto_atributos", joinColumns = @JoinColumn(name = "produto_id"))
    @MapKeyColumn(name = "atributo_chave")
    @Column(name = "atributo_valor")
    private Map<String, String> atributosEspecificos;

    @ManyToOne
    @JsonIgnore
    private Estoque estoque;

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