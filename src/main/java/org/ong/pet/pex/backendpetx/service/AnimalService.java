package org.ong.pet.pex.backendpetx.service;

import org.ong.pet.pex.backendpetx.dto.request.AnimalGenericoRequisicao;
import org.ong.pet.pex.backendpetx.dto.request.AnimalObituarioResquisicao;
import org.ong.pet.pex.backendpetx.dto.response.AnimalGenericoResposta;
import org.ong.pet.pex.backendpetx.dto.response.AnimalPaginadoResposta;
import org.ong.pet.pex.backendpetx.enums.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface AnimalService {

    void adicionarAdocaoConjuntaEmAnimal (Map<String,Long> ids);

    AnimalGenericoResposta atualizarAnimal (Long id, AnimalGenericoRequisicao animalSemConjuntoDTO);

    void deletarPorId (Long id);

    void declararObito (AnimalObituarioResquisicao obiturario);

    AnimalGenericoResposta buscarAnimalPorId(Long id);

    AnimalGenericoResposta buscarAnimalPorChip(String chip);

    Page<AnimalPaginadoResposta> paginarAnimais(String nome, String raca, EspecieEnum especie, PorteEnum porte, SaudeEnum saudeEnum,
                                                String comportamento, MaturidadeEnum maturidade,
                                                OrigemAnimalEnum origem, SexoEnum sexo, AdocaoEnum adocaoEnum, Pageable pageable);

    Long contarQuantidadeAnimais();

    // Atualizações parciais (página detalhe)
    void atualizarPerfilBasico(Long id, String nome, String raca, String especie, String porte,
                               String sexo, String maturidade, String origem, String corPelagem);

    void atualizarResumoSaude(Long id, String doencasLista);

    AnimalGenericoResposta salvarAnimal(AnimalGenericoRequisicao animalGenericoRequisicao);

    Page<AnimalPaginadoResposta> paginarAnimaisParaAdocao(String nome, String raca, EspecieEnum especie, PorteEnum porte, SaudeEnum saudeEnum,
                                               String comportamento, MaturidadeEnum maturidade,
                                                OrigemAnimalEnum origem, SexoEnum sexo, Pageable pageable);

    long getTotalAdocoes();

}
