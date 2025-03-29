package org.ong.pet.pex.backendpetx.service.mappers;


import lombok.AllArgsConstructor;
import org.ong.pet.pex.backendpetx.dto.request.BoletimDTORequisicao;
import org.ong.pet.pex.backendpetx.dto.request.MaezinhaComFilhotesDTO;
import org.ong.pet.pex.backendpetx.dto.response.AnimalGenericoResposta;
import org.ong.pet.pex.backendpetx.dto.response.BoletimDTOResposta;
import org.ong.pet.pex.backendpetx.entities.Boletim;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BoletimMapper {

    private final AnimalMapper animalMapper;

    public BoletimDTOResposta converteParaDTO(Boletim entidade) {
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
                .animal(AnimalGenericoResposta.builder()
                        .id(entidade.getAnimal().getId())
                        .chipId(entidade.getAnimal().getChipId())
                        .nome(entidade.getAnimal().getNome())
                        .maturidade(entidade.getAnimal().getMaturidadeEnum().toString())
                        .raca(entidade.getAnimal().getRaca())
                        .sexo(entidade.getAnimal().getSexoEnum().toString())
                        .origem(entidade.getAnimal().getOrigemEnum().toString())
                        .porte(entidade.getAnimal().getPorteEnum().toString())
                        .comportamento(entidade.getAnimal().getComportamentoEnum().toString())
                        .especie(entidade.getAnimal().getEspecieEnum().toString())
                        .doencas(entidade.getAnimal().getDoencas())
                        .status(entidade.getAnimal().getStatusEnum().toString())
                        .maezinhaComFilhotes(new MaezinhaComFilhotesDTO(
                                entidade.getAnimal().getMaezinhaComFilhotes().getQuantidadeFemeas(),
                                entidade.getAnimal().getMaezinhaComFilhotes().getQuantidadeMachos()))
                        .build()
                )
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
                .animal(AnimalMapper.converterParaAnimal(dto.getAnimal()))
                .build();
    }




}