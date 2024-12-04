package org.ong.pet.pex.backendpetx.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ong.pet.pex.backendpetx.enums.ComportamentoEnum;
import org.ong.pet.pex.backendpetx.enums.EspecieEnum;
import org.ong.pet.pex.backendpetx.enums.MaturidadeEnum;
import org.ong.pet.pex.backendpetx.enums.OrigemAnimalEnum;
import org.ong.pet.pex.backendpetx.enums.PorteEnum;
import org.ong.pet.pex.backendpetx.enums.SexoEnum;
import org.ong.pet.pex.backendpetx.enums.StatusEnum;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "animal_tb")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Animal extends EntidadeBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(unique = true)
    private String chipId;

    @Column(name = "nome")
    private String nome;

    @Column(name = "raca")
    private String raca;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo")
    private SexoEnum sexoEnum;

    @Enumerated(EnumType.STRING)
    @Column(name= "maturidade")
    private MaturidadeEnum maturidadeEnum;

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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "animal_doencas", joinColumns = @JoinColumn(name = "animal_id"))
    @Column(name = "doenca")
    private Set<String> doencas = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusEnum statusEnum;

    @ManyToOne(cascade = {CascadeType.ALL,   CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "id_ong")
    private Ong ong;


    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JsonIgnore
    @JoinTable(
            name = "animal_tutores",
            joinColumns = @JoinColumn(name = "animal_id"),
            inverseJoinColumns = @JoinColumn(name = "tutor_id")
    )
    private Set<Tutor> tutores;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Animal animal = (Animal) o;
        return Objects.equals(chipId, animal.chipId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), chipId);
    }


}