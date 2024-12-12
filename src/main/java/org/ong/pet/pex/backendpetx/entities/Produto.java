package org.ong.pet.pex.backendpetx.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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