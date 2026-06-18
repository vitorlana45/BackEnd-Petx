package org.ong.pet.pex.backendpetx.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ong.pet.pex.backendpetx.enums.Destino;
import org.ong.pet.pex.backendpetx.enums.OrigemAnimalEnum;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoletimRequisicao{

    private Long numeroOcorrencia;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
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
    private AnimalGenericoRequisicao animal;
}