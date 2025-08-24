package org.ong.pet.pex.backendpetx.entities;

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
import java.util.Objects;

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
@Table(name = "boletins")
public class Boletim extends EntidadeBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    private boolean arquivado = false;

    @Column(name = "arquivado_em")
    private OffsetDateTime arquivadoEm;

    @Column(name = "motivo_arquivamento", length = 120)
    private String motivoArquivamento;

    @Column(name = "arquivado_por", length = 100)
    private String arquivadoPor;

    @Enumerated(EnumType.STRING)
    private Destino destino;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, orphanRemoval = true)
    @JoinColumn(name = "animal_id")
    private Animal animal;


    @ManyToOne(fetch = FetchType.LAZY, cascade = {MERGE, PERSIST, REFRESH})
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @JoinColumn(name = "ong_id"
    )
    private Ong ong;

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