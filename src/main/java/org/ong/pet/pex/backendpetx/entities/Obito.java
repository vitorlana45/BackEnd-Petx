package org.ong.pet.pex.backendpetx.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "obito_tb")
public class Obito extends EntidadeBase {

    @Column(name = "data_obito")
    private Date dataObito;

    @Column(name = "motivo_obito")
    private String motivoObito;

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "id_animal")
    private Animal animal;

    @Column(updatable = false)
    private LocalDateTime criadoEm;

    private LocalDateTime atualizadoEm;

}
