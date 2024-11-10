package org.ong.pet.pex.backendpetx.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import org.ong.pet.pex.backendpetx.enums.*;

import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "animal_tb")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_animal")
    private Long id;

    @Column(name = "nome", unique = true)
    private String nome;

    @Column(name = "idade")
    private Integer idade;

    @Column(name = "raca")
    private String raca;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo")
    private SexoEnum sexoEnum;

    @Enumerated(EnumType.STRING)
    @Column(name = "origem")
    private OrigemAnimalEnum origemEnum;

    @Enumerated(EnumType.STRING)
    @Column(name = "porte")
    private PorteEnum porteEnum;

    @Enumerated(EnumType.STRING)
    @Column(name = "comportamento")
    private ComportamentoEnum comportamentoEnum;

    @Enumerated(EnumType.STRING)
    @Column(name = "especie")
    private EspecieEnum especieEnum;

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

    @ManyToMany
    @JoinTable(
            name = "animal_conjunto_adocao",
            joinColumns = @JoinColumn(name = "animal_id"),
            inverseJoinColumns = @JoinColumn(name = "animal_conjunto_id")
    )
    @JsonManagedReference
    private Set<Animal> animalConjunto = new HashSet<>();

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "animal_tutores",
            joinColumns = @JoinColumn(name = "animal_id"),
            inverseJoinColumns = @JoinColumn(name = "tutor_id")
    )
    private Set<Tutor> tutores;

     @OneToOne
     private Obituario obituario;
}
