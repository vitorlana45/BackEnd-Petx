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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "produto")
public class Produto extends EntidadeBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    private String nome;

    private String descricao;

    private Long quantidade;

    @Enumerated(EnumType.STRING)
    private UnidadeDeMedidaEnum unidadeDeMedida;

    @Enumerated(EnumType.STRING)
    private TipoProduto tipoProduto;

    @ElementCollection
    @CollectionTable(name = "produto_atributos", joinColumns = @JoinColumn(name = "produto_id"))
    @MapKeyColumn(name = "atributo_nome")
    @Column(name = "atributo_valor")
    private Map<String, String> atributos = new HashMap<>();

    @ManyToOne
    @JsonIgnore
    private Estoque estoque;

    public void adicionarAtributo(String chave, String valor) {
        atributos.put(chave, valor);
    }

    public String obterAtributo(String chave) {
        return atributos.get(chave);
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