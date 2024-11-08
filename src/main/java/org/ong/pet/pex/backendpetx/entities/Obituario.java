package org.ong.pet.pex.backendpetx.entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "obito_tb")
public class Obituario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_obito")
    private Long id;

    @Column(name = "status")
    private boolean status;

    @Column(name = "data_obito")
    private Date dataObito;

    @Column(name = "motivo_obito")
    private String motivoObito;
}
