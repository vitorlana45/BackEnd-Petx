package org.ong.pet.pex.backendpetx.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ong.pet.pex.backendpetx.enums.TipoProduto;
import org.ong.pet.pex.backendpetx.enums.UnidadeDeMedidaEnum;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "produto")
public class Produto extends EntidadeBase {
    private String nome;
    private String descricao;
    private Long quantidade;
    @Enumerated(EnumType.STRING)
    private UnidadeDeMedidaEnum unidadeDeMedida;
    @Enumerated(EnumType.STRING)
    private TipoProduto tipoProduto;

    @Column(columnDefinition = "text")
    private String metaData;

    @ManyToOne
    private Estoque estoque;
}