package org.ong.pet.pex.backendpetx.service.impl;

import org.ong.pet.pex.backendpetx.dto.request.*;
import org.ong.pet.pex.backendpetx.entities.Animal;
import org.ong.pet.pex.backendpetx.enums.*;
import org.ong.pet.pex.backendpetx.repositories.AnimalRepository;
import org.ong.pet.pex.backendpetx.service.AnimalService;
import org.ong.pet.pex.backendpetx.service.exceptions.AnimalJaCadastrado;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnimalServiceImpl implements AnimalService {

    private final AnimalRepository animalRepository;

    public AnimalServiceImpl(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    // apenas funcionará quando todos os animais forem cadastrados pela primeira vez
    // vamos discutir e ver compensa manter esse metodo
    public AnimalDTO cadastrarAnimalComConjunto(AnimalDTO animalDTO) {
        if (animalRepository.findAnimalByNome(animalDTO.nome()) != null) {
            throw new AnimalJaCadastrado("Animal com o nome: " + animalDTO.nome() + " já cadastrado");
        }

        if (animalDTO.animalConjunto() != null) {
            animalDTO.animalConjunto().forEach(conjunto -> {
                if (animalRepository.findAnimalByNome(conjunto.nome()) != null) {
                    throw new AnimalJaCadastrado("Animal conjunto com o nome: " + conjunto.nome() + " já cadastrado");
                }
            });
        }

        Animal animal = converterParaAnimal(animalDTO);


        if (animal.getAnimalConjunto() != null) {
            animalRepository.saveAll(animal.getAnimalConjunto());
        }

        animalRepository.save(animal);

        return animalDTO;
    }

    @Override
    public List<AnimalDTO> animaisCadastrados() {
        return List.of();
    }


    @Override
    public AnimalDTO atualizarAnimal(Long id) {
        return null;
    }

    @Override
    public void deletarPorId(Long id) {

    }

    @Override
    public void declararObito(Long id) {

    }

    private Animal converterParaAnimal(AnimalDTO animalDTO) {
        Animal animal = new Animal();
        animal.setNome(animalDTO.nome());
        animal.setRaca(animalDTO.raca().toUpperCase());
        animal.setIdade(animalDTO.idade());
        animal.setEspecieEnum(EspecieEnum.valueOf(animalDTO.especieEnum().toString().toUpperCase()));
        animal.setPorteEnum(PorteEnum.valueOf(animalDTO.porteEnum().toString().toUpperCase()));
        animal.setSexoEnum(SexoEnum.valueOf(animalDTO.sexoEnum().toString().toUpperCase()));
        animal.setOrigemEnum(OrigemAnimalEnum.valueOf(animalDTO.origemEnum().toString().toUpperCase()));
        animal.setComportamentoEnum(ComportamentoEnum.valueOf(animalDTO.comportamentoEnum().toString().toUpperCase()));

        if (animalDTO.animalConjunto() != null && !animalDTO.animalConjunto().isEmpty()) {
            Set<Animal> conjuntoDeAnimais = animalDTO.animalConjunto().stream()
                    .map(this::converterAnimalConjunto)
                    .collect(Collectors.toSet());
            animal.setAnimalConjunto(conjuntoDeAnimais);
        }
        return animal;
    }

    private Animal converterAnimalConjunto(AnimalConjuntoDTO conjuntoDTO) {
        Animal animalConjunto = new Animal();
        animalConjunto.setNome(conjuntoDTO.nome());
        animalConjunto.setRaca(conjuntoDTO.raca().toUpperCase());
        animalConjunto.setIdade(conjuntoDTO.idade());
        animalConjunto.setEspecieEnum(EspecieEnum.valueOf(conjuntoDTO.especieEnum().toString().toUpperCase()));
        animalConjunto.setPorteEnum(PorteEnum.valueOf(conjuntoDTO.porteEnum().toString().toUpperCase()));
        animalConjunto.setSexoEnum(SexoEnum.valueOf(conjuntoDTO.sexoEnum().toString().toUpperCase()));
        animalConjunto.setOrigemEnum(OrigemAnimalEnum.valueOf(conjuntoDTO.origemEnum().toString().toUpperCase()));
        animalConjunto.setComportamentoEnum(ComportamentoEnum.valueOf(conjuntoDTO.comportamentoEnum().toString().toUpperCase()));
        return animalConjunto;
    }

}
