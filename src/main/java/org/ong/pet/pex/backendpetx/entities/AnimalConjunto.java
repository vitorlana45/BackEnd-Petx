package org.ong.pet.pex.backendpetx.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "animal_conjunto_tb")
public class AnimalConjunto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "animal_conjunto_id")
    private Long animalConjuntoId;

    // Relacionamento com o animal principal
    @ManyToOne
    @JoinColumn(name = "animal_principal_id", referencedColumnName = "id", nullable = false)
    private Animal animalPrincipal;

    // Relacionamento com o animal associado
    @ManyToOne
    @JoinColumn(name = "animal_relacionamento_id", referencedColumnName = "id", nullable = false)
    private Animal animalRelacionamento;
}
