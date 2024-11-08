package org.ong.pet.pex.backendpetx.entities;

import jakarta.persistence.*;
import lombok.*;
import org.ong.pet.pex.backendpetx.enums.*;

import java.util.Set;

@Entity
@Table(name = "animal_db")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;
    private String nome;
    private Integer idade;
    private String  raca;
    private SexoEnum sexoEnum;
    private PorteEnum porteEnum;
    private ComportamentoEnum comportamentoEnum;
    private EspecieEnum especieEnum;
    @ManyToMany
    @JoinTable(name = "animal_tutores",
    joinColumns = @JoinColumn(name = "animal_id"),
    inverseJoinColumns = @JoinColumn(name = "tutor_id"))
    private Set<Tutor> tutores;

}