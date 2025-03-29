package org.ong.pet.pex.backendpetx.service.mappers;

import lombok.AllArgsConstructor;
import org.ong.pet.pex.backendpetx.dto.request.AnimalDTO;
import org.ong.pet.pex.backendpetx.dto.request.AnimalGenericoRequisicao;
import org.ong.pet.pex.backendpetx.dto.response.AnimalGenericoResposta;
import org.ong.pet.pex.backendpetx.dto.response.RespostaAnimalSemConjunto;
import org.ong.pet.pex.backendpetx.entities.Animal;
import org.ong.pet.pex.backendpetx.entities.incorporarEntidades.MaezinhaComFilhotes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class AnimalMapper {

    private static final Logger logger = LoggerFactory.getLogger(AnimalMapper.class);

    public static AnimalGenericoResposta converterParaRespostaAnimalComConjuntoDTO(Animal animal) {
        logger.info("Iniciando a conversão para AnimalGenericoResposta para retornar ao cliente");
        var resposta = AnimalGenericoResposta.builder()
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
                .listaAnimaisConjunto(null)
                .build();

        if (animal.getDoencas() != null) {
            resposta.setDoencas(new HashSet<>(animal.getDoencas()));
        }

        return resposta;

    }

    public static Set<AnimalGenericoResposta> converterParaListaDeAnimaisComConjuntoDTO(Set<Animal> animais) {
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

    public AnimalGenericoResposta mapeiaAnimalEListaParaRetorno  (Animal animal, List<Animal> lsAnimais) {
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
                .listaAnimaisConjunto(lsAnmaisConjunto)
                .build();
    }

    public static Animal converterParaAnimal(AnimalGenericoRequisicao dto) {
        return Animal.builder()
                .chipId(dto.getChipId())
                .nome(dto.getNome())
                .raca(dto.getRaca())
                .maturidadeEnum(dto.getMaturidade())
                .sexoEnum(dto.getSexo())
                .origemEnum(dto.getOrigem())
                .porteEnum(dto.getPorte())
                .comportamentoEnum(dto.getComportamento())
                .especieEnum(dto.getEspecie())
                .doencas(dto.getDoencas())
                .maezinhaComFilhotes(new MaezinhaComFilhotes(
                        dto.getMaezinhaComFilhotesDTO().getQuantidadeMacho(),
                        dto.getMaezinhaComFilhotesDTO().getQuantidadeFemea()
                ))
                .condicaoAnimal(dto.getCondicaoAnimal())
                .doencas(dto.getDoencas())
                .statusEnum(dto.getStatus())
                .build();
    }
}