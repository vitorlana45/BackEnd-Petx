package org.ong.pet.pex.backendpetx.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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

    @Column(updatable = false)
    private LocalDateTime criadoEm;

    private LocalDateTime atualizadoEm;


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
