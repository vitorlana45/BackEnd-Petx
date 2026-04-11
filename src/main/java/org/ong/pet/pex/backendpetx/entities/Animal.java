package org.ong.pet.pex.backendpetx.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.ong.pet.pex.backendpetx.entities.incorporarEntidades.MaezinhaComFilhotes;
import org.ong.pet.pex.backendpetx.enums.*;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static jakarta.persistence.CascadeType.*;


@SQLDelete(sql = "UPDATE animal_tb SET arquivado = true, arquivado_em = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "arquivado = false")
@Audited
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

    private String imagemPrincipalPerfil;

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

    @Column(name = "adotado", nullable = false)
    @Enumerated(EnumType.STRING)
    private AdocaoEnum adotado = AdocaoEnum.DISPONIVEL;

    @Column(nullable = false)
    private boolean arquivado = false;

    @Column(name = "arquivado_em")
    private OffsetDateTime arquivadoEm;

    @Column(name = "motivo_arquivamento", length = 120)
    private String motivoArquivamento;

    @Column(name = "arquivado_por", length = 100)
    private String arquivadoPor;

    // Tornar LAZY para não carregar todas as doenças em listagens paginadas
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "animal_doencas", joinColumns = @JoinColumn(name = "animal_id"))
    @Column(name = "doenca")
    @Builder.Default
    private Set<String> doencas = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "saude")
    private SaudeEnum saudeEnum;

    @Column(name = "condicao_animal")
    private String condicaoAnimal;

    private MaezinhaComFilhotes maezinhaComFilhotes;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {MERGE, REFRESH})
    @JoinColumn(name = "id_ong")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Ong ong;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "boletim_id",
            foreignKey = @ForeignKey(name = "fk_animal_boletim"))
    private Boletim boletim;
    @ManyToMany(cascade = {DETACH, MERGE, PERSIST, REFRESH})


    @JoinTable(name = "animal_tutores",
            joinColumns = @JoinColumn(name = "animal_id"),
            inverseJoinColumns = @JoinColumn(name = "tutor_id"))
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED) // <- se Tutor NÃO for @Audited
    private Set<Tutor> tutores = new HashSet<>();

    public void setChipId(String chipId) {
        // If chipId is null or empty, store as null
        this.chipId = (chipId == null || chipId.trim().isEmpty()) ? null : chipId.trim();
    }


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