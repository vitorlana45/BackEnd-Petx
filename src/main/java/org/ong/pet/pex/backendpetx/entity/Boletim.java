package org.ong.pet.pex.backendpetx.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.ong.pet.pex.backendpetx.enums.Destino;
import org.ong.pet.pex.backendpetx.enums.OrigemAnimalEnum;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static jakarta.persistence.CascadeType.*;


@Audited
@Entity
@SQLDelete(sql = "UPDATE boletins SET arquivado = true, arquivado_em = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "arquivado = false")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "boletins",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_boletins_numero_ocorrencia", columnNames = {"numero_ocorrencia"})
    })
public class Boletim extends EntidadeBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_ocorrencia", nullable = false, unique = true)
    private Long numeroOcorrencia;

    private LocalDateTime dataAtendimento;

    private String motivoRecolhimento;

    @Enumerated(EnumType.STRING)
    private OrigemAnimalEnum origem;

    private String nomeDenuncianteOuTutor;

    private String cpfDenuncianteOuTutor;

    private String telefoneDenuncianteOuTutor;

    private String observacaoClinica;

    private String ruaAvenida;

    private String cidade;

    private String municipio;

    private String bairro;

    private String estado;

    @Column(nullable = false)
    @lombok.Builder.Default
    private boolean arquivado = false;

    @Column(name = "arquivado_em")
    private OffsetDateTime arquivadoEm;

    @Column(name = "motivo_arquivamento", length = 120)
    private String motivoArquivamento;

    @Column(name = "arquivado_por", length = 100)
    private String arquivadoPor;

    @Enumerated(EnumType.STRING)
    private Destino destino;

    @OneToMany(mappedBy = "boletim", cascade = {PERSIST, MERGE, REFRESH}, orphanRemoval = true)
    @Builder.Default
    private Set<Animal> animais = new HashSet<>();


    @ManyToOne(fetch = FetchType.LAZY, cascade = {MERGE, REFRESH})
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @JoinColumn(name = "ong_id")
    private Ong ong;

    /**
     * Compatibilidade com o binding atual do formulário (boletim.animal.*)
     * sem impedir que uma ocorrência tenha múltiplos animais.
     */
    public Animal getAnimal() {
        return animais.stream().findFirst().orElse(null);
    }

    /**
     * Mantém o comportamento antigo: ao setar "animal" substitui o principal.
     */
    public void setAnimal(Animal animal) {
        this.animais.clear();
        if (animal != null) {
            addAnimal(animal);
        }
    }

    public void addAnimal(Animal animal) {
        if (animal == null) return;
        this.animais.add(animal);
        animal.setBoletim(this);
    }

    public void removeAnimal(Animal animal) {
        if (animal == null) return;
        this.animais.remove(animal);
        if (animal.getBoletim() == this) {
            animal.setBoletim(null);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Boletim boletim = (Boletim) o;
        return Objects.equals(id, boletim.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}