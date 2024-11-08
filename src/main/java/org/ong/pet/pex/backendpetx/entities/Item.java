package org.ong.pet.pex.backendpetx.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "item_db")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String descricao;
    private int quantidade;

}
