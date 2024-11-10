package org.ong.pet.pex.backendpetx.entities;

import jakarta.persistence.*;

import java.time.*;
import java.util.*;

@Entity
@Table(name = "obituario_tb")
public class Obituario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_obito")
    private Long id;

    @Column(name = "data_obito")
    private Date dataObito;

    @Column(name = "motivo_obito")
    private String motivoObito;

    @ManyToMany(mappedBy = "obituarios")
    private Set<Animal> animais = new HashSet<>();

    @Column(updatable = false)
    private LocalDateTime criadoEm;

    private LocalDateTime atualizadoEm;

    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
        atualizadoEm = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        atualizadoEm = LocalDateTime.now();
    }

}
