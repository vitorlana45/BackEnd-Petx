package org.ong.pet.pex.backendpetx.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "estoque_tb")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Estoque extends EntidadeBase {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @OneToMany(mappedBy = "estoque",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @lombok.Builder.Default
    private List<CategoriaEstoque> categorias = new ArrayList<>();

    @OneToMany(mappedBy = "estoque",
            cascade = CascadeType.ALL,
            orphanRemoval = false)
    @Builder.Default
    private List<Produto> produtos = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonIgnore
    private Ong ong;

    /** Helper para manter os dois lados do relacionamento sincronizados */
    public void addCategoria(CategoriaEstoque cat) {
        if (cat == null) return;
        if (this.getCategorias() == null) this.setCategorias(new ArrayList<>());
        this.getCategorias().add(cat);
        cat.setEstoque(this);
    }

    public void removeCategoria(CategoriaEstoque cat) {
        if (cat == null) return;
        categorias.remove(cat);
        cat.setEstoque(null);
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Estoque)) return false;
        return Objects.equals(id, ((Estoque) o).id);
    }
    @Override public int hashCode() { return Objects.hashCode(id); }
}
