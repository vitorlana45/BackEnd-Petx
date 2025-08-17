package org.ong.pet.pex.backendpetx.service.impl;

import org.ong.pet.pex.backendpetx.dto.request.BoletimDTORequisicao;
import org.ong.pet.pex.backendpetx.dto.response.BoletimDTOResposta;
import org.ong.pet.pex.backendpetx.entities.Animal;
import org.ong.pet.pex.backendpetx.entities.Boletim;
import org.ong.pet.pex.backendpetx.enums.Destino;
import org.ong.pet.pex.backendpetx.enums.SexoEnum;
import org.ong.pet.pex.backendpetx.repositories.AnimalRepository;
import org.ong.pet.pex.backendpetx.repositories.BoletimRepository;
import org.ong.pet.pex.backendpetx.repositories.OngRepository;
import org.ong.pet.pex.backendpetx.service.BoletimService;
import org.ong.pet.pex.backendpetx.service.exceptions.PetXException;
import org.ong.pet.pex.backendpetx.service.mappers.AnimalMapper;
import org.ong.pet.pex.backendpetx.service.mappers.BoletimMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BoletimServiceImpl implements BoletimService {

    private final BoletimRepository boletimRepository;

    private final AnimalRepository animalRepository;

    private final BoletimMapper boletimMapper;

    private final OngRepository ongRepository;

    public BoletimServiceImpl(BoletimRepository boletimRepository, AnimalRepository animalRepository, BoletimMapper boletimMapper, OngRepository ongRepository) {
        this.boletimRepository = boletimRepository;
        this.animalRepository = animalRepository;
        this.boletimMapper = boletimMapper;
        this.ongRepository = ongRepository;
    }

    @Override
    @Transactional
    public BoletimDTOResposta createBoletim(BoletimDTORequisicao boletimDTO) {


        if( boletimDTO.getAnimal().isAnimalEMaezinha() && !boletimDTO.getAnimal().getSexo().equals(SexoEnum.FEMEA))
            throw new PetXException("Animal não pode ser maezinha se não for fêmea");

        if(!boletimDTO.getAnimal().isAnimalEMaezinha() && boletimDTO.getAnimal().getMaezinhaComFilhotes() != null)
            throw new PetXException("Animal não pode ter filhotes se não for maezinha");

        System.out.println("BoletimDTO: " + boletimDTO);

        Boletim boletim = boletimMapper.converteParaEntidade(boletimDTO);

        if (boletimDTO.getAnimal() == null)
            throw new PetXException("Animal não pode ser nulo");

        Animal newAnimal = AnimalMapper.converterParaAnimal(boletimDTO.getAnimal());
        newAnimal.setOng(ongRepository.findById(1L).orElse(null));
        newAnimal = animalRepository.save(newAnimal);
        
        boletim.setAnimal(newAnimal);
        boletim.setOng(newAnimal.getOng());
        
        // Save Boletim entity
        boletim = boletimRepository.save(boletim);
        
        // Set the bidirectional relationship
        newAnimal.setBoletim(boletim);
        animalRepository.save(newAnimal);
        
        return boletimMapper.converteParaDTO(boletim);
    }

    @Override
    @Transactional(readOnly = true)
    public BoletimDTOResposta getBoletim(Long id) {
        Boletim boletim = boletimRepository.findById(id)
                .orElseThrow(() -> new PetXException("Boletim não encontrado"));
        return boletimMapper.converteParaDTO(boletim);
    }

    @Transactional
    @Override
    public void deleteBoletim(Long id) {
        Boletim boletim = boletimRepository.findById(id)
                .orElseThrow(() -> new PetXException("Boletim não encontrado"));

        if (boletim.getAnimal() != null) {
            boletim.getAnimal().setOng(null);
            boletim.getAnimal().setBoletim(null);
            boletim.setAnimal(null);
        }
        boletim.setOng(null);
        boletimRepository.delete(boletim);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BoletimDTOResposta> findAllBoletins(
                                                    Long numeroOcorrencia,
//                                                    LocalDateTime dataInicio,
//                                                    LocalDateTime dataFim,
                                                    Destino destino,
                                                    Pageable pageable) {
//        String destinoStr = destino != null ? destino : "";
        Page<Boletim> boletins = boletimRepository.findAllBoletins(numeroOcorrencia, destino != null ? destino.name() : null, pageable);
        System.out.println("asdasd");
        return boletins.map(boletimMapper::converteParaDTO);
    }

    @Override
    @Transactional
    public BoletimDTOResposta updateBoletim(Long id, BoletimDTORequisicao dto) {
        Boletim boletim = boletimRepository.findById(id)
                .orElseThrow(() -> new PetXException("Boletim não encontrado"));

        aplicarCamposPresentes(dto, boletim);

        if(dto.getAnimal() != null){
            Animal newAnimal = AnimalMapper.converterParaAnimal(dto.getAnimal());
            newAnimal.setOng(ongRepository.findById(1L).orElse(null));
            newAnimal = animalRepository.save(newAnimal);
            boletim.setAnimal(newAnimal);
            
            // Set the bidirectional relationship
            newAnimal.setBoletim(boletim);
            animalRepository.save(newAnimal);
        }

        boletim = boletimRepository.save(boletim);
        return boletimMapper.converteParaDTO(boletim);
    }

    private void aplicarCamposPresentes(BoletimDTORequisicao dto, Boletim boletim) {
        Optional.ofNullable(dto.getNumeroOcorrencia()).ifPresent(boletim::setNumeroOcorrencia);
        Optional.ofNullable(dto.getDataAtendimento()).ifPresent(boletim::setDataAtendimento);
        Optional.ofNullable(dto.getDestino()).ifPresent(boletim::setDestino);
        Optional.ofNullable(dto.getMotivoRecolhimento()).ifPresent(boletim::setMotivoRecolhimento);
        Optional.ofNullable(dto.getObservacaoClinica()).ifPresent(boletim::setObservacaoClinica);
        Optional.ofNullable(dto.getRuaAvenida()).ifPresent(boletim::setRuaAvenida);
        Optional.ofNullable(dto.getCidade()).ifPresent(boletim::setCidade);
        Optional.ofNullable(dto.getMunicipio()).ifPresent(boletim::setMunicipio);
        Optional.ofNullable(dto.getBairro()).ifPresent(boletim::setBairro);
        Optional.ofNullable(dto.getEstado()).ifPresent(boletim::setEstado);
        Optional.ofNullable(dto.getNomeDenuncianteOuTutor()).ifPresent(boletim::setNomeDenuncianteOuTutor);
        Optional.ofNullable(dto.getCpfDenuncianteOuTutor()).ifPresent(boletim::setCpfDenuncianteOuTutor);
        Optional.ofNullable(dto.getTelefoneDenuncianteOuTutor()).ifPresent(boletim::setTelefoneDenuncianteOuTutor);
    }

}