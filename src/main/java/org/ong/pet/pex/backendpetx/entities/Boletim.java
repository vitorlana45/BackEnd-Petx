package org.ong.pet.pex.backendpetx.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ong.pet.pex.backendpetx.entities.incorporarEntidades.MaezinhaComFilhotes;
import org.ong.pet.pex.backendpetx.enums.Destino;
import org.ong.pet.pex.backendpetx.enums.OrigemAnimalEnum;

import java.time.LocalDateTime;

@Entity
@Table(name = "boletins")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Boletim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long numeroOcorrencia;

    private LocalDateTime dataAtendimento;

    private String motivoRecolhimento;

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

    private Destino destino;

    private MaezinhaComFilhotes maezinhaComFilhotes;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "animal_id", referencedColumnName = "id")
    private Animal animal;

    @ManyToOne
    @JoinColumn(name = "ong_id")
    private Ong ong;
}