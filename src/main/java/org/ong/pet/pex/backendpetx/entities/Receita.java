package org.ong.pet.pex.backendpetx.entities;

import jakarta.persistence.*;
import lombok.*;
import org.ong.pet.pex.backendpetx.enums.FormaPagamentoEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "receitas_tb")
public class Receita extends EntidadeBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "descricao", nullable = false)
    private String descricao; // Ex: Doação do Fulano, Venda de Rifa

    @Column(name = "categoria")
    private String categoria; // Ex: Doação, Evento, Venda

    @Column(nullable = false, name = "valor")
    private BigDecimal valor;

    @Column(name = "data_recebimento")
    private LocalDate dataRecebimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento")
    private FormaPagamentoEnum formaPagamento;

    @Column(name = "origem")
    private String origem; // Ex: Nome do Doador, Empresa X

    @Column(name = "detalhes_personalizados", columnDefinition = "TEXT")
    private String detalhesPersonalizados; // JSON com campos extras

    @ManyToOne(fetch = FetchType.EAGER)
    private Ong ong;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Receita receita = (Receita) o;
        return Objects.equals(id, receita.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

