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

    // Situação de adoção
    private String adotado;

    // Boletim vinculado
    private Long boletimId;
    private Long boletimNumero;

    // Tutores
    private int tutoresCount;

    // ONG
    private String ongNome;

    // Arquivamento
    private boolean arquivado;
    private LocalDateTime arquivadoEm;
    private String arquivadoPor;
    private String motivoArquivamento;

}
