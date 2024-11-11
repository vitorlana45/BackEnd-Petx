package org.ong.pet.pex.backendpetx.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cadastro_despesas")
public class Despesa extends EntidadeBase {

    @Column(nullable = false, name = "motivo")
    private String motivo;

    @Column(nullable = false, name = "valor")
    private BigDecimal valor;

    @Column(name = "nome_empresa")
    private String nomeEmpresa;

    @Column(name = "data_prevista")
    private LocalDate dataPrevista;

    @Column(name = "forma_pagamento")
    private String formaPagamento;

    @Column(name = "custo_para_animal")
    private Boolean custoParaAnimal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id")
    private Animal animalAssociado;

    @Column(name = "pago")
    private Boolean pago;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    @ManyToOne(fetch = FetchType.EAGER)
    private Ong ong;

}
