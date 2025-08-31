package org.ong.pet.pex.backendpetx.service.mappers;

import lombok.AllArgsConstructor;
import org.ong.pet.pex.backendpetx.dto.request.AnimalGenericoRequisicao;
import org.ong.pet.pex.backendpetx.dto.request.MaezinhaComFilhotesDTO;
import org.ong.pet.pex.backendpetx.dto.response.AnimalGenericoResposta;
import org.ong.pet.pex.backendpetx.dto.response.AnimalPaginadoResposta;
import org.ong.pet.pex.backendpetx.dto.response.RespostaAnimalSemConjunto;
import org.ong.pet.pex.backendpetx.entities.Animal;
import org.ong.pet.pex.backendpetx.entities.incorporarEntidades.MaezinhaComFilhotes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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
                .maturidade(animal.getMaturidadeEnum() != null ? animal.getMaturidadeEnum().toString() : null)
                .raca(animal.getRaca())
                .sexo(animal.getSexoEnum() != null ? animal.getSexoEnum().toString() : null)
                .origem(animal.getOrigemEnum() != null ? animal.getOrigemEnum().toString() : null)
                .porte(animal.getPorteEnum() != null ? animal.getPorteEnum().toString() : null)
                .comportamento(animal.getComportamento())
                .especie(animal.getEspecieEnum() != null ? animal.getEspecieEnum().toString() : null)
                .doencas(animal.getDoencas())
                .status(animal.getSaudeEnum() != null ? animal.getSaudeEnum().toString() : null)
                .condicaoAnimal(animal.getCondicaoAnimal())
                .corPelagem(animal.getCorPelagem())
                .imagemPrincipalPerfil(animal.getImagemPrincipalPerfil())
                .maezinhaComFilhotes(converteMaezinhaParaDTO(animal.getMaezinhaComFilhotes()))
                .listaAnimaisConjunto(null)
                .dataCadastro(animal.getCriadoEm())
                .build();

        // Removido setDoencas redundante
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
        if (animalGenericoRequisicao.getDoencas() != null) {
            animal.getDoencas().addAll(animalGenericoRequisicao.getDoencas());
        }
        animal.setSaudeEnum(animalGenericoRequisicao.getSaude());
        animal.setCorPelagem(animalGenericoRequisicao.getCorPelagem());
        animal.setCondicaoAnimal(animalGenericoRequisicao.getCondicaoAnimal());
        // Trata imagem: usa nome original do arquivo como placeholder de armazenamento
        if (animalGenericoRequisicao.getImagemPrincipalPerfil() != null && !animalGenericoRequisicao.getImagemPrincipalPerfil().isEmpty()) {
            animal.setImagemPrincipalPerfil(animalGenericoRequisicao.getImagemPrincipalPerfil().getOriginalFilename());
        }
        animal.setMaezinhaComFilhotes(converteMaezinhaParaEntidade(animalGenericoRequisicao.getMaezinhaComFilhotes()));
        return animal;

    }

    public static RespostaAnimalSemConjunto converterParaAnimalSemConjunto(Animal animal) {
        return new RespostaAnimalSemConjunto(
                animal.getId(),
                animal.getChipId(),
                animal.getNome(),
                animal.getMaturidadeEnum() != null ? animal.getMaturidadeEnum().toString() : null,
                animal.getRaca(),
                animal.getSexoEnum() != null ? animal.getSexoEnum().toString() : null,
                animal.getOrigemEnum() != null ? animal.getOrigemEnum().toString() : null,
                animal.getPorteEnum() != null ? animal.getPorteEnum().toString() : null,
                animal.getComportamento(),
                animal.getEspecieEnum() != null ? animal.getEspecieEnum().toString() : null,
                animal.getDoencas(),
                animal.getSaudeEnum() != null ? animal.getSaudeEnum().toString() : null,
                null, // destino não mapeado na entidade, manter null
                animal.getCorPelagem(),
                converteMaezinhaParaDTO(animal.getMaezinhaComFilhotes()),
                animal.getImagemPrincipalPerfil()
        );
    }

    public AnimalGenericoResposta mapeiaAnimalEListaParaRetorno  (Animal animal, List<Animal> lsAnimais) {
        var lsAnmaisConjunto = lsAnimais.stream()
                .map(x -> AnimalGenericoResposta.builder()
                        .id(x.getId())
                        .chipId(x.getChipId())
                        .nome(x.getNome())
                        .maturidade(x.getMaturidadeEnum() != null ? x.getMaturidadeEnum().getMaturidade() : null)
                        .raca(x.getRaca())
                        .sexo(x.getSexoEnum() != null ? x.getSexoEnum().getSexo() : null)
                        .origem(x.getOrigemEnum() != null ? x.getOrigemEnum().getOrigemAnimal() : null)
                        .porte(x.getPorteEnum() != null ? x.getPorteEnum().getPorte() : null)
                        .comportamento(x.getComportamento())
                        .especie(x.getEspecieEnum() != null ? x.getEspecieEnum().getEspecie() : null)
                        .doencas(x.getDoencas())
                        .condicaoAnimal(x.getCondicaoAnimal())
                        .corPelagem(x.getCorPelagem())
                        .imagemPrincipalPerfil(x.getImagemPrincipalPerfil())
                        .maezinhaComFilhotes(converteMaezinhaParaDTO(x.getMaezinhaComFilhotes()))
                        .status(x.getSaudeEnum() != null ? x.getSaudeEnum().getStatus() : null)
                        .build())
                .collect(Collectors.toList());

        return AnimalGenericoResposta.builder()
                .id(animal.getId())
                .chipId(animal.getChipId())
                .nome(animal.getNome())
                .maturidade(animal.getMaturidadeEnum() != null ? animal.getMaturidadeEnum().getMaturidade() : null)
                .raca(animal.getRaca())
                .sexo(animal.getSexoEnum() != null ? animal.getSexoEnum().getSexo() : null)
                .origem(animal.getOrigemEnum() != null ? animal.getOrigemEnum().getOrigemAnimal() : null)
                .porte(animal.getPorteEnum() != null ? animal.getPorteEnum().getPorte() : null)
                .comportamento(animal.getComportamento())
                .doencas(animal.getDoencas())
                .maezinhaComFilhotes(converteMaezinhaParaDTO(animal.getMaezinhaComFilhotes()))
                .especie(animal.getEspecieEnum() != null ? animal.getEspecieEnum().getEspecie() : null)
                .status(animal.getSaudeEnum() != null ? animal.getSaudeEnum().getStatus() : null)
                .corPelagem(animal.getCorPelagem())
                .condicaoAnimal(animal.getCondicaoAnimal())
                .imagemPrincipalPerfil(animal.getImagemPrincipalPerfil())
                .listaAnimaisConjunto(lsAnmaisConjunto)
                .build();
    }

    public static Animal converterParaAnimal(AnimalGenericoRequisicao dto) {
        return Animal.builder()
                .chipId(dto.getChipId()) // The setter will handle null/empty conversion
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
                .saudeEnum(dto.getSaude())
                .corPelagem(dto.getCorPelagem())
                .maezinhaComFilhotes(converteMaezinhaParaEntidade(dto.getMaezinhaComFilhotes())
                )
                .imagemPrincipalPerfil(dto.getImagemPrincipalPerfil() != null && !dto.getImagemPrincipalPerfil().isEmpty() ? dto.getImagemPrincipalPerfil().getOriginalFilename() : null)
                .build();
    }

    public static AnimalGenericoResposta converterParaAnimalGenericoResposta(Animal an) {
        return AnimalGenericoResposta.builder()
                .chipId(an.getChipId())
                .nome(an.getNome())
                .raca(an.getRaca())
                .maturidade(an.getMaturidadeEnum() != null ? an.getMaturidadeEnum().getMaturidade() : null)
                .sexo(an.getSexoEnum() != null ? an.getSexoEnum().getSexo() : null)
                .origem(an.getOrigemEnum() != null ? an.getOrigemEnum().getOrigemAnimal() : null)
                .porte(an.getPorteEnum() != null ? an.getPorteEnum().getPorte() : null)
                .comportamento(an.getComportamento())
                .especie(an.getEspecieEnum() != null ? an.getEspecieEnum().getEspecie() : null)
                .doencas(an.getDoencas())
                .condicaoAnimal(an.getCondicaoAnimal())
                .status(an.getSaudeEnum() != null ? String.valueOf(an.getSaudeEnum()) : null)
                .corPelagem(an.getCorPelagem())
                .condicaoAnimal(an.getCondicaoAnimal())
                .imagemPrincipalPerfil(an.getImagemPrincipalPerfil())
                .maezinhaComFilhotes(
                        an.getMaezinhaComFilhotes() != null ? 
                        converteMaezinhaParaDTO(an.getMaezinhaComFilhotes()) : null
                )
                .build();
    }

    public static MaezinhaComFilhotesDTO converteMaezinhaParaDTO (MaezinhaComFilhotes maezinhaComFilhotes) {
        if (maezinhaComFilhotes != null) {
            return new org.ong.pet.pex.backendpetx.dto.request.MaezinhaComFilhotesDTO(
                    maezinhaComFilhotes.getQuantidadeFemeas(),
                    maezinhaComFilhotes.getQuantidadeMachos()
            );
        }
        return null;
    }

    public static MaezinhaComFilhotes converteMaezinhaParaEntidade (org.ong.pet.pex.backendpetx.dto.request.MaezinhaComFilhotesDTO maezinhaComFilhotesDTO) {
        if (maezinhaComFilhotesDTO != null) {
            Integer quantidadeFemea = maezinhaComFilhotesDTO.getQuantidadeFemea();
            Integer quantidadeMacho = maezinhaComFilhotesDTO.getQuantidadeMacho();

            return new MaezinhaComFilhotes(
                    quantidadeFemea != null ? quantidadeFemea : 0,
                    quantidadeMacho != null ? quantidadeMacho : 0
            );
        }
        return null;
    }

    public static List<AnimalPaginadoResposta> converteAnimaisParaAnimalPaginadoResposta(List<Animal> animais) {
        return animais.stream()
                .map(animal -> AnimalPaginadoResposta.builder()
                        .id(animal.getId())
                        .chipId(animal.getChipId())
                        .imagemPrincipalPerfil(animal.getImagemPrincipalPerfil())
                        .nome(animal.getNome())
                        .raca(animal.getRaca())
                        .maturidade(animal.getMaturidadeEnum() != null ? animal.getMaturidadeEnum().toString() : null)
                        .sexo(animal.getSexoEnum() != null ? animal.getSexoEnum().toString() : null)
                        .origem(animal.getOrigemEnum() != null ? animal.getOrigemEnum().toString() : null)
                        .porte(animal.getPorteEnum() != null ? animal.getPorteEnum().toString() : null)
                        .comportamento(animal.getComportamento())
                        .especie(animal.getEspecieEnum() != null ? animal.getEspecieEnum().toString() : null)
                        .doencas(animal.getDoencas())
                        .saude(animal.getSaudeEnum() != null ? animal.getSaudeEnum().toString() : null)
                        .adotado(animal.getAdotado() != null ? animal.getAdotado().toString() : null)
                        .build())
                .collect(Collectors.toList());
    }
}