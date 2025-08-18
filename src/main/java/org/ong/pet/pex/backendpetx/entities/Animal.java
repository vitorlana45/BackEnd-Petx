package org.ong.pet.pex.backendpetx.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.ong.pet.pex.backendpetx.entities.incorporarEntidades.MaezinhaComFilhotes;
import org.ong.pet.pex.backendpetx.enums.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Builder
@Entity
@Table(name = "animal_tb", 
       uniqueConstraints = {
           @UniqueConstraint(
               name = "unique_chip_id_when_not_null",
               columnNames = {"chip_id"}
           )
       })
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Animal extends EntidadeBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    // Allow null values, but enforce uniqueness for non-null values with a custom index
    @Column(name = "chip_id")
    private String chipId;
    
    /**
     * Sets the chipId and converts empty strings to null to avoid unique constraint issues
     */
    public void setChipId(String chipId) {
        // If chipId is null or empty, store as null
        this.chipId = (chipId == null || chipId.trim().isEmpty()) ? null : chipId.trim();
    }

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

    @Column(name = "comportamento")
    private String comportamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "especie")
    private EspecieEnum especieEnum;

    @Column(name = "cor_pelagem")
    private String corPelagem;

    // Tornar LAZY para não carregar todas as doenças em listagens paginadas
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "animal_doencas", joinColumns = @JoinColumn(name = "animal_id"))
    @Column(name = "doenca")
    @Builder.Default
    private Set<String> doencas = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusEnum statusEnum;

    @Column(name = "condicao_animal")
    private String condicaoAnimal;

    private MaezinhaComFilhotes maezinhaComFilhotes;

    // Evitar carregamento automático em listagens: trocar fetch padrão (EAGER em ManyToOne) para LAZY.
    // Cascade ALL em ManyToOne tende a propagar operações indesejadas; manter apenas MERGE/PERSIST/REFRESH se necessário.
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "id_ong")
    private Ong ong;

    // Este relacionamento originalmente EAGER + recíproco em Boletim gerava queries profundas repetidas.
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "id_boletim")
    private Boletim boletim;

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