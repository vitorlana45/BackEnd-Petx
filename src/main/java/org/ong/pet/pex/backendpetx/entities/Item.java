package org.ong.pet.pex.backendpetx.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "item_tb")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item")
    private Long id;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "quantidade")
    private int quantidade;
}
