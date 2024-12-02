package org.ong.pet.pex.backendpetx.service.mappers;

import org.ong.pet.pex.backendpetx.dto.request.AnimalGenericoRequisicao;
import org.ong.pet.pex.backendpetx.dto.response.AnimalGenericoResposta;
import org.ong.pet.pex.backendpetx.dto.response.RespostaAnimalSemConjunto;
import org.ong.pet.pex.backendpetx.entities.Animal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AnimalMapper {

    private static final Logger logger = LoggerFactory.getLogger(AnimalMapper.class);

    public static AnimalGenericoResposta converterParaRespostaAnimalComConjuntoDTO(Animal animal) {
        logger.info("Iniciando a conversão para AnimalGenericoResposta para retornar ao cliente");
        return AnimalGenericoResposta.builder()
                .id(animal.getId())
                .chipId(animal.getChipId())
                .nome(animal.getNome())
                .maturidade(animal.getMaturidadeEnum().toString())
                .raca(animal.getRaca())
                .sexo(animal.getSexoEnum().toString())
                .origem(animal.getOrigemEnum().toString())
                .porte(animal.getPorteEnum().toString())
                .comportamento(animal.getComportamentoEnum().toString())
                .especie(animal.getEspecieEnum().toString())
                .doencas(animal.getDoencas())
                .status(animal.getStatusEnum().toString())
                .lsAnimaisConjunto(null)
                .build();

    }

    public static Set<AnimalGenericoResposta> converterParaListaDeAnimaisComConjuntoDTO(List<Animal> animais) {
        return animais.stream()
                .map(AnimalMapper::converterParaRespostaAnimalComConjuntoDTO)
                .collect(Collectors.toSet());
    }

    public static Animal converterParaAnimalSemConjunto(Animal animal, AnimalGenericoRequisicao animalGenericoRequisicao) {
        animal.setChipId(animalGenericoRequisicao.getChipId());
        animal.setNome(animalGenericoRequisicao.getNome());
        animal.setMaturidadeEnum(animalGenericoRequisicao.getMaturidade());
        animal.setRaca(animalGenericoRequisicao.getRaca());
        animal.setEspecieEnum(animalGenericoRequisicao.getEspecie());
        animal.setPorteEnum(animalGenericoRequisicao.getPorte());
        animal.setSexoEnum(animalGenericoRequisicao.getSexo());
        animal.setOrigemEnum(animalGenericoRequisicao.getOrigem());
        animal.setComportamentoEnum(animalGenericoRequisicao.getComportamento());
        animal.getDoencas().addAll(animalGenericoRequisicao.getDoencas());
        animal.setStatusEnum(animalGenericoRequisicao.getStatus());
        return animal;

    }

    public static RespostaAnimalSemConjunto converterParaAnimalSemConjunto(Animal animal) {
        return new RespostaAnimalSemConjunto(
                animal.getId(),
                animal.getChipId(),
                animal.getNome(),
                animal.getMaturidadeEnum().toString(),
                animal.getRaca(),
                animal.getSexoEnum().toString(),
                animal.getOrigemEnum().toString(),
                animal.getPorteEnum().toString(),
                animal.getComportamentoEnum().toString(),
                animal.getEspecieEnum().toString(),
                animal.getDoencas(),
                animal.getStatusEnum().toString()

        );
    }
}