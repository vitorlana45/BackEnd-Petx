package org.ong.pet.pex.backendpetx.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ong.pet.pex.backendpetx.dto.request.MaezinhaComFilhotesDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AnimalGenericoResposta {

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
    private MaezinhaComFilhotesDTO maezinhaComFilhotes;
    private List<AnimalGenericoResposta> listaAnimaisConjunto = new ArrayList<>();
}
