package org.ong.pet.pex.backendpetx.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AnimalGenericoResposta {

    private Long id;
    private String nome;
    private Integer idade;
    private String raca;
    private String sexo;
    private String origem;
    private String porte;
    private String comportamento;
    private String especie;
    private Set<String> doencas;
    private boolean estaVivo;
    private Set<AnimalGenericoResposta> animaisConjunto;

}
