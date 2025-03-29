package org.ong.pet.pex.backendpetx.service.mappers;

import lombok.AllArgsConstructor;
import org.ong.pet.pex.backendpetx.dto.request.AnimalGenericoRequisicao;
import org.ong.pet.pex.backendpetx.dto.request.MaezinhaComFilhotesDTO;
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
                .comportamento(animal.getComportamento())
                .especie(animal.getEspecieEnum().toString())
                .doencas(animal.getDoencas())
                .status(animal.getStatusEnum().toString())
                .maezinhaComFilhotes(converteMaezinhaParaDTO(animal.getMaezinhaComFilhotes()))
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
        animal.setComportamento(animalGenericoRequisicao.getComportamento());
        animal.getDoencas().addAll(animalGenericoRequisicao.getDoencas());
        animal.setStatusEnum(animalGenericoRequisicao.getStatus());
        animal.setCorPelagem(animalGenericoRequisicao.getCorPelagem());
        animal.setMaezinhaComFilhotes(converteMaezinhaParaEntidade(animalGenericoRequisicao.getMaezinhaComFilhotes()));
        return animal;

    }

    public static RespostaAnimalSemConjunto converterParaAnimalSemConjunto(Animal animal) {
        return RespostaAnimalSemConjunto.builder()
                .id(animal.getId())
                .chipId(animal.getChipId())
                .nome(animal.getNome())
                .maturidade(animal.getMaturidadeEnum().toString())
                .raca(animal.getRaca())
                .sexo(animal.getSexoEnum().toString())
                .origem(animal.getOrigemEnum().toString())
                .porte(animal.getPorteEnum().toString())
                .comportamento(animal.getComportamento())
                .especie(animal.getEspecieEnum().toString())
                .doencas(animal.getDoencas())
                .status(animal.getStatusEnum().toString())
                .corPelagem(animal.getCorPelagem())
                .maezinhaComFilhotes(converteMaezinhaParaDTO(animal.getMaezinhaComFilhotes()))
                .build();
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
                        .comportamento(x.getComportamento())
                        .especie(x.getEspecieEnum().getEspecie())
                        .doencas(x.getDoencas())
                        .maezinhaComFilhotes(converteMaezinhaParaDTO(x.getMaezinhaComFilhotes()))
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
                .comportamento(animal.getComportamento())
                .doencas(animal.getDoencas())
                .maezinhaComFilhotes(converteMaezinhaParaDTO(animal.getMaezinhaComFilhotes()))
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
                .comportamento(dto.getComportamento())
                .especieEnum(dto.getEspecie())
                .doencas(dto.getDoencas())
                .condicaoAnimal(dto.getCondicaoAnimal())
                .statusEnum(dto.getStatus())
                .corPelagem(dto.getCorPelagem())
                .maezinhaComFilhotes(converteMaezinhaParaEntidade(dto.getMaezinhaComFilhotes())
                )
                .build();
    }

    public static MaezinhaComFilhotesDTO converteMaezinhaParaDTO (MaezinhaComFilhotes maezinhaComFilhotes) {
        if (maezinhaComFilhotes != null) {
            return new MaezinhaComFilhotesDTO(
                    maezinhaComFilhotes.getQuantidadeFemeas(),
                    maezinhaComFilhotes.getQuantidadeMachos()
            );
        }
        return null;
    }

    public static MaezinhaComFilhotes converteMaezinhaParaEntidade (MaezinhaComFilhotesDTO maezinhaComFilhotesDTO) {
        if (maezinhaComFilhotesDTO != null) {
            return new MaezinhaComFilhotes(
                    maezinhaComFilhotesDTO.getQuantidadeFemea(),
                    maezinhaComFilhotesDTO.getQuantidadeMacho()
            );
        }
        return null;
    }
}