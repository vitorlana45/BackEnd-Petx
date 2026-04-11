package org.ong.pet.pex.backendpetx.service.mappers;

import lombok.AllArgsConstructor;
import org.ong.pet.pex.backendpetx.dto.request.BoletimDTORequisicao;
import org.ong.pet.pex.backendpetx.dto.request.MaezinhaComFilhotesDTO;
import org.ong.pet.pex.backendpetx.dto.response.AnimalGenericoResposta;
import org.ong.pet.pex.backendpetx.dto.response.BoletimDTOResposta;
import org.ong.pet.pex.backendpetx.entities.Boletim;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class BoletimMapper {

    private final AnimalMapper animalMapper;

    public BoletimDTOResposta converteParaDTO(Boletim entidade) {
        new AnimalGenericoResposta();
        var animal = entidade.getAnimal();
        return BoletimDTOResposta.builder()
                .id(entidade.getId())
                .bairro(entidade.getBairro())
                .cidade(entidade.getCidade())
                .estado(entidade.getEstado())
                .ruaAvenida(entidade.getRuaAvenida())
                .nomeDenuncianteOuTutor(entidade.getNomeDenuncianteOuTutor())
                .telefoneDenuncianteOuTutor(entidade.getTelefoneDenuncianteOuTutor())
                .origem(entidade.getOrigem())
                .numeroOcorrencia(entidade.getNumeroOcorrencia())
                .observacaoClinica(entidade.getObservacaoClinica())
                .cpfDenuncianteOuTutor(entidade.getCpfDenuncianteOuTutor())
                .motivoRecolhimento(entidade.getMotivoRecolhimento())
                .municipio(entidade.getMunicipio())
                .dataAtendimento(entidade.getDataAtendimento())
                .dataAtendimento(entidade.getDataAtendimento())
                .destino(entidade.getDestino())
            .animal(animal == null ? null : AnimalGenericoResposta.builder()
                .id(animal.getId())
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
                .corPelagem(animal.getCorPelagem())
                .condicaoAnimal(animal.getCondicaoAnimal())
                .maezinhaComFilhotes(animal.getMaezinhaComFilhotes() != null ?
                    MaezinhaComFilhotesDTO.builder()
                        .quantidadeFemea(animal.getMaezinhaComFilhotes().getQuantidadeFemeas())
                        .quantidadeMacho(animal.getMaezinhaComFilhotes().getQuantidadeMachos())
                        .build()
                    : null)
                .build())
                .build();
    }

    public Boletim converteParaEntidade(BoletimDTORequisicao dto) {
        return Boletim.builder()
                .numeroOcorrencia(dto.getNumeroOcorrencia())
                .dataAtendimento(dto.getDataAtendimento())
                .origem(dto.getOrigem())
                .motivoRecolhimento(dto.getMotivoRecolhimento())
                .bairro(dto.getBairro())
                .cidade(dto.getCidade())
                .estado(dto.getEstado())
                .ruaAvenida(dto.getRuaAvenida())
                .nomeDenuncianteOuTutor(dto.getNomeDenuncianteOuTutor())
                .telefoneDenuncianteOuTutor(dto.getTelefoneDenuncianteOuTutor())
                .cpfDenuncianteOuTutor(dto.getCpfDenuncianteOuTutor())
                .observacaoClinica(dto.getObservacaoClinica())
                .municipio(dto.getMunicipio())
                .destino(dto.getDestino())
                .build();
    }

    public List<BoletimDTOResposta> converteParaDTO(List<Boletim> boletins) {
        return boletins.stream()
                .map(this::converteParaDTO)
                .collect(Collectors.toList());
    }
}