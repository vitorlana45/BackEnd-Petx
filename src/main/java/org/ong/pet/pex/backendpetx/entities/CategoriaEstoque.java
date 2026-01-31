package org.ong.pet.pex.backendpetx.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
@Entity
@Table(
        name = "categoria_estoque_tb",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_categoria_por_estoque_nome", columnNames = {"estoque_id", "nome"})
        }
)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaEstoque extends EntidadeBase {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "estoque_id", nullable = false)
    private Estoque estoque;

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoriaEstoque)) return false;
        return Objects.equals(id, ((CategoriaEstoque) o).id);
    }
    @Override public int hashCode() { return Objects.hashCode(id); }
}
