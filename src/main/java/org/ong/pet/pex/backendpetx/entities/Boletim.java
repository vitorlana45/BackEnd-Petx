package org.ong.pet.pex.backendpetx.entities;

import jakarta.persistence.*;
import lombok.*;
import org.ong.pet.pex.backendpetx.enums.Destino;
import org.ong.pet.pex.backendpetx.enums.OrigemAnimalEnum;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
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

    @Enumerated(EnumType.STRING)
    private Destino destino;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "animal_id")
    private Animal animal;

    @ManyToOne
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