package org.ong.pet.pex.backendpetx.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ong.pet.pex.backendpetx.enums.CategoriaDespesaEnum;
import org.ong.pet.pex.backendpetx.enums.FormaPagamentoEnum;
import org.ong.pet.pex.backendpetx.enums.StatusDespesaEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "despesas_tb")
public class Despesa extends EntidadeBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "categoria_despesa")
    private CategoriaDespesaEnum categoria;

    @Column(name = "descricao")
    private String descricao;

    @Column(nullable = false, name = "valor")
    private BigDecimal valor;

    @Column(name = "data_prevista_pagamento")
    private LocalDate dataPrevistaPagamento;

    @Column(name = "forma_pagamento")
    private FormaPagamentoEnum formaPagamento;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_despesa")
    private StatusDespesaEnum statusDespesa;

    @ManyToOne(fetch = FetchType.EAGER)
    private Ong ong;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Despesa despesa = (Despesa) o;
        return Objects.equals(id, despesa.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
