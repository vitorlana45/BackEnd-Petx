package org.ong.pet.pex.backendpetx.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
public class AnimalPaginadoResposta {

    private Long id;
    private String chipId;
    private String nome;
    private String raca;
    private String maturidade;
    private String sexo;
    private String origem;
    private String porte;
    private String comportamento;
    private String especie;
    private Set<String> doencas;
    private String status;
    private List<AnimalPaginadoResposta> listaAnimaisConjunto;
}
