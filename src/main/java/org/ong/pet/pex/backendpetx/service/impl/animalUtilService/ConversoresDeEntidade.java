package org.ong.pet.pex.backendpetx.service.impl.animalUtilService;

import org.ong.pet.pex.backendpetx.dto.request.AnimalConjuntoDTO;
import org.ong.pet.pex.backendpetx.dto.request.AnimalDTO;
import org.ong.pet.pex.backendpetx.dto.response.AnimalGenericoResposta;
import org.ong.pet.pex.backendpetx.entities.Animal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ConversoresDeEntidade {

    private static final Logger logger = LoggerFactory.getLogger(ConversoresDeEntidade.class);

    public static Animal converterParaAnimal(AnimalDTO animalDTO) {

        logger.info("Iniciando a conversão para AnimalDTO");

        Animal animal = new Animal();
        animal.setNome(animalDTO.getNome());
        animal.setRaca(animalDTO.getRaca());
        animal.setIdade(animalDTO.getIdade());
        animal.setEspecieEnum(animalDTO.getEspecie());
        animal.setPorteEnum(animalDTO.getPorte());
        animal.setSexoEnum(animalDTO.getSexo());
        animal.setOrigemEnum(animalDTO.getOrigem());
        animal.setComportamentoEnum(animalDTO.getComportamento());
        animal.getDoencas().addAll(animalDTO.getDoencas());
        animal.setEstaVivo(animalDTO.isEstaVivo());


        if (animal.getAnimalConjunto() == null) {
            animal.setAnimalConjunto(new HashSet<>());
        }


        if (animalDTO.getAnimalConjunto() != null) {
            for (AnimalConjuntoDTO conjuntoDTO : animalDTO.getAnimalConjunto()) {
                Animal animalConjunto = converterAnimalConjuntoParaAnimal(conjuntoDTO);
                animal.getAnimalConjunto().add(animalConjunto);
            }
        }

        return animal;
    }

    private static Animal converterAnimalConjuntoParaAnimal(AnimalConjuntoDTO conjuntoDTO) {
        Animal animalConjunto = new Animal();
        animalConjunto.setNome(conjuntoDTO.getNome());
        animalConjunto.setRaca(conjuntoDTO.getRaca() != null ? conjuntoDTO.getRaca().toUpperCase() : null);
        animalConjunto.setIdade(conjuntoDTO.getIdade());
        animalConjunto.setEspecieEnum(conjuntoDTO.getEspecie());
        animalConjunto.setPorteEnum(conjuntoDTO.getPorte());
        animalConjunto.setSexoEnum(conjuntoDTO.getSexo());
        animalConjunto.setOrigemEnum(conjuntoDTO.getOrigem());
        animalConjunto.setComportamentoEnum(conjuntoDTO.getComportamento());
        animalConjunto.getDoencas().addAll(conjuntoDTO.getDoencas());
        animalConjunto.setEstaVivo(conjuntoDTO.isEstaVivo());

        // Inicializar o conjunto vazio para evitar NullPointerException
        animalConjunto.setAnimalConjunto(new HashSet<>());

        return animalConjunto;
    }

    public static AnimalGenericoResposta converterParaRespostaAnimalComConjuntoDTO(Animal animal) {
        logger.info("Iniciando a conversão para AnimalGenericoResposta para retornar ao cliente");
        return new AnimalGenericoResposta(
                animal.getId(),
                animal.getNome(),
                animal.getIdade(),
                animal.getRaca(),
                animal.getSexoEnum().toString(),
                animal.getOrigemEnum().toString(),
                animal.getPorteEnum().toString(),
                animal.getComportamentoEnum().toString(),
                animal.getEspecieEnum().toString(),
                animal.getDoencas(),
                animal.isEstaVivo(),
                converterApenasAListasDosAnimaisConjunto(animal)
        );
    }

    public static Set<AnimalGenericoResposta> converterApenasAListasDosAnimaisConjunto(Animal animal) {
        return animal.getAnimalConjunto().stream()
                .map(animalConjunto -> new AnimalGenericoResposta(
                        animalConjunto.getId(),
                        animalConjunto.getNome(),
                        animalConjunto.getIdade(),
                        animalConjunto.getRaca(),
                        animalConjunto.getSexoEnum().toString(),
                        animalConjunto.getOrigemEnum().toString(),
                        animalConjunto.getPorteEnum().toString(),
                        animalConjunto.getComportamentoEnum().toString(),
                        animalConjunto.getEspecieEnum().toString(),
                        animalConjunto.getDoencas(),
                        animalConjunto.isEstaVivo(),
                        null
                ))
                .collect(Collectors.toSet());
    }

    public static Set<AnimalGenericoResposta> converterParaListaDeAnimaisComConjuntoDTO(Set<Animal> animais) {
        return animais.stream()
                .map(ConversoresDeEntidade::converterParaRespostaAnimalComConjuntoDTO)
                .collect(Collectors.toSet());
    }

}