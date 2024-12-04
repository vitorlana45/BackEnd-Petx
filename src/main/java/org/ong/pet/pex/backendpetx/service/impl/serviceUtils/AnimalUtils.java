// AnimalUtils.java
package org.ong.pet.pex.backendpetx.service.impl.serviceUtils;

import org.ong.pet.pex.backendpetx.dto.response.AnimalGenericoResposta;
import org.ong.pet.pex.backendpetx.entities.Animal;
import org.ong.pet.pex.backendpetx.repositories.AnimalConjuntoRepository;
import org.ong.pet.pex.backendpetx.repositories.AnimalRepository;
import org.ong.pet.pex.backendpetx.service.exceptions.PetXException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.ong.pet.pex.backendpetx.service.mappers.AnimalMapper.converterParaRespostaAnimalComConjuntoDTO;

@Component
public class AnimalUtils {

    private final AnimalRepository animalRepository;
    private final AnimalConjuntoRepository animalConjuntoRepository;

    public AnimalUtils(AnimalRepository animalRepository, AnimalConjuntoRepository animalConjuntoRepository) {
        this.animalRepository = animalRepository;
        this.animalConjuntoRepository = animalConjuntoRepository;
    }



    public List<Animal> buscarAnimaisPorId(List<Long> ids){
        List<Animal> animais = new ArrayList<>();
        for (Long id : ids) {
            animais.add(animalRepository.findAnimalById(id).orElseThrow(() -> PetXException.animalNaoEncontrado(id.toString())));
        }
        return animais;
    }


    @Transactional(readOnly = true)
    public AnimalGenericoResposta buscarAnimalPorIdComConjuntoResposta(Long id) {
        // ** Recupera o animal pelo id ** //
        List<Animal> lsAnimais = new ArrayList<>();
        var animal = animalRepository.findById(id).orElseThrow(() -> PetXException.animalNaoEncontrado(id.toString()));

        // ** Recupera o conjunto do animal ** //
        var optConjuntoAnimal = animalConjuntoRepository.findByAnimalPrincipalIdOrAnimalRelacionamentoId(animal.getId());

        // ** Recupera os animais atrelado ao principal ** //
        if (!optConjuntoAnimal.isEmpty()) {
            // ** Recupera o id do animal principal ** //
            var idAnimalPrincipal = optConjuntoAnimal.getFirst().getAnimalPrincipalId();
            // ** Recupera o conjunto de animais atrelados ao principal ** //
            var lsConjuntoAnimais = animalConjuntoRepository.findByAnimalPrincipalId(idAnimalPrincipal);
            // ** Adiciona o animal principal a lista de retorno ** //
            lsAnimais.add(animalRepository.findById(idAnimalPrincipal).orElseThrow(() -> PetXException.animalNaoEncontrado(id.toString())));
            // ** Adiciona os demais animais do conjunto a lista de retorno ** //
            lsConjuntoAnimais.forEach(animalConjunto -> {
                lsAnimais.add(animalRepository.findById(animalConjunto.getAnimalRelacionamentoId()).orElseThrow(() ->  PetXException.animalNaoEncontrado(id.toString())));
            });
            // ** Remove o id do animal consultado do retorno de conjunto ** //
            lsAnimais.removeIf(it -> it.getId().equals(id));
        }

        if(optConjuntoAnimal.isEmpty()) {
            return converterParaRespostaAnimalComConjuntoDTO(animalRepository.findById(animal.getId()).orElseThrow(() -> PetXException.animalNaoEncontrado(id.toString())));
        }

        return mapeiaParaRetorno(animal, lsAnimais);
    }

    private AnimalGenericoResposta mapeiaParaRetorno(Animal animal, List<Animal> lsAnimais) {
        var lsAnmaisConjunto = lsAnimais.stream()
                .map(x -> AnimalGenericoResposta.builder()
                        .id(x.getId())
                        .chipId(x.getChipId())
                        .nome(x.getNome())
                        .maturidade(x.getMaturidadeEnum().getMaturidade())
                        .raca(x.getRaca())
                        .sexo(x.getSexoEnum().getSexo())
                        .origem(x.getOrigemEnum().getOrigemAnimal())
                        .porte(x.getPorteEnum().getPorte())
                        .comportamento(x.getComportamentoEnum().getComportamento())
                        .especie(x.getEspecieEnum().getEspecie())
                        .doencas(x.getDoencas())
                        .status(x.getStatusEnum().getStatus())
                        .build())
                .collect(Collectors.toList());

        return AnimalGenericoResposta.builder()
                .id(animal.getId())
                .chipId(animal.getChipId())
                .nome(animal.getNome())
                .maturidade(animal.getMaturidadeEnum().getMaturidade())
                .raca(animal.getRaca())
                .sexo(animal.getSexoEnum().getSexo())
                .origem(animal.getOrigemEnum().getOrigemAnimal())
                .porte(animal.getPorteEnum().getPorte())
                .comportamento(animal.getComportamentoEnum().getComportamento())
                .doencas(animal.getDoencas())
                .especie(animal.getEspecieEnum().getEspecie())
                .status(animal.getStatusEnum().getStatus())
                .lsAnimaisConjunto(lsAnmaisConjunto)
                .build();
    }
}