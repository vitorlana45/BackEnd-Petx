package org.ong.pet.pex.backendpetx.dto.response;

import lombok.*;
import org.ong.pet.pex.backendpetx.dto.request.MaezinhaComFilhotesDTO;

import java.time.LocalDateTime;
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
    private String corPelagem;
    private String condicaoAnimal;
    private String imagemPrincipalPerfil;
    private MaezinhaComFilhotesDTO maezinhaComFilhotes;
    private List<AnimalGenericoResposta> listaAnimaisConjunto = new ArrayList<>();
    private LocalDateTime dataCadastro;

}
