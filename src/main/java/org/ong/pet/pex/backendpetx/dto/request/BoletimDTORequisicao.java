package org.ong.pet.pex.backendpetx.dto.request;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import lombok.*;
import org.ong.pet.pex.backendpetx.enums.Destino;
import org.ong.pet.pex.backendpetx.enums.OrigemAnimalEnum;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoletimDTORequisicao{

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
    private AnimalGenericoRequisicao animal;
}