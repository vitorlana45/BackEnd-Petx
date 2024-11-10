package org.ong.pet.pex.backendpetx.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import org.ong.pet.pex.backendpetx.enums.*;

import java.time.LocalDateTime;
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

    @ElementCollection
    @CollectionTable(name = "animal_doencas", joinColumns = @JoinColumn(name = "animal_id"))
    @Column(name = "doenca")
    private Set<String> doencas;

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

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
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

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "animal_obituarios",
            joinColumns = @JoinColumn(name = "animal_id"),
            inverseJoinColumns = @JoinColumn(name = "obituario_id")
    )
    private Set<Obituario> obituarios = new HashSet<>();
}
