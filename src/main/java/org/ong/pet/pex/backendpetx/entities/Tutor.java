package org.ong.pet.pex.backendpetx.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Tutor extends Usuario {

    @Column(name = "idade")
    private Integer idade;

    @Column(name = "telefone")
    private Integer telefone;

    @Column(name = "criado_em", nullable = false)
    private Instant criado_em;

    @Column(name = "atualizado_em")
    private Instant atualizado_em;

    @PrePersist
    public void prePersist() {
        if (criado_em == null) {
            criado_em = Instant.now();
        }
        atualizado_em = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        atualizado_em = Instant.now();
    }

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "tutor_animal_tb",
            joinColumns = @JoinColumn(name = "tutor_id"),
            inverseJoinColumns = @JoinColumn(name = "animal_id")
    )
    private Set<Animal> animais;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "contato_id")
    private Contato contato;
}
