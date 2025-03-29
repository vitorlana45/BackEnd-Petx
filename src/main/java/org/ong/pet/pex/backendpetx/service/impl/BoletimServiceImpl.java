package org.ong.pet.pex.backendpetx.service.impl;

import org.ong.pet.pex.backendpetx.dto.request.BoletimDTORequisicao;
import org.ong.pet.pex.backendpetx.dto.response.BoletimDTOResposta;
import org.ong.pet.pex.backendpetx.entities.Animal;
import org.ong.pet.pex.backendpetx.entities.Boletim;
import org.ong.pet.pex.backendpetx.enums.SexoEnum;
import org.ong.pet.pex.backendpetx.repositories.AnimalRepository;
import org.ong.pet.pex.backendpetx.repositories.BoletimRepository;
import org.ong.pet.pex.backendpetx.service.BoletimService;
import org.ong.pet.pex.backendpetx.service.exceptions.PetXException;
import org.ong.pet.pex.backendpetx.service.mappers.AnimalMapper;
import org.ong.pet.pex.backendpetx.service.mappers.BoletimMapper;
import org.springframework.stereotype.Service;

@Service
public class BoletimServiceImpl implements BoletimService {

    private final BoletimRepository boletimRepository;

    private final AnimalRepository animalRepository;

    private final BoletimMapper boletimMapper;

    public BoletimServiceImpl(BoletimRepository boletimRepository, AnimalRepository animalRepository, BoletimMapper boletimMapper) {
        this.boletimRepository = boletimRepository;
        this.animalRepository = animalRepository;
        this.boletimMapper = boletimMapper;
    }

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
        newAnimal = animalRepository.save(newAnimal);
        boletim.setAnimal(newAnimal);
        // Save Boletim entity
        boletim = boletimRepository.save(boletim);
        return boletimMapper.converteParaDTO(boletim);
    }

    @Override
    public BoletimDTOResposta getBoletim(Long id) {
        return null;
    }

    @Override
    public void deleteBoletim(Long id) {

    }

    @Override
    public BoletimDTOResposta updateBoletim(Long id, BoletimDTORequisicao dto) {
        return null;
    }
}