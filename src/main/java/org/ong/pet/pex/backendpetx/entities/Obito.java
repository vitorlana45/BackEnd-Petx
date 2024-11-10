package org.ong.pet.pex.backendpetx.entities;

import jakarta.persistence.*;

import java.time.*;
import java.util.*;

@Entity
@Table(name = "obito_tb")
public class Obito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_obito")
    private Long id;

    @Column(name = "data_obito")
    private Date dataObito;

    @Column(name = "motivo_obito")
    private String motivoObito;

    @OneToOne
    @JoinColumn(name = "id_animal", referencedColumnName = "id_animal")
    private Animal animal;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Obito obito = (Obito) o;
        return Objects.equals(id, obito.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
