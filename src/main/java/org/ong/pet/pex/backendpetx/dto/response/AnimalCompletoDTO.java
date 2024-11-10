package org.ong.pet.pex.backendpetx.dto.response;

import lombok.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimalCompletoDTO {
    private Long id;
    private String nome;
    private Integer idade;
    private String raca;
    private String sexo;
    private String origem;
    private String porte;
    private String comportamento;
    private String especie;
    private boolean statusVivo;
    private Set<String> tutores;
    private Set<AnimalCompletoDTO> animalConjunto;
}