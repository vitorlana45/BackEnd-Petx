package org.ong.pet.pex.backendpetx.entities;

import jakarta.persistence.*;
import lombok.*;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria_despesa")
    private CategoriaDespesaEnum categoria;

    @Column(name = "descricao")
    private String descricao;

    @Column(nullable = false, name = "valor")
    private BigDecimal valor;

    @Column(name = "data_prevista_pagamento")
    private LocalDate dataPrevistaPagamento;

    @Enumerated(EnumType.STRING)
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
