package org.ong.pet.pex.backendpetx.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.ong.pet.pex.backendpetx.dto.request.AnimalDTO;
import org.ong.pet.pex.backendpetx.dto.response.AnimalGenericoResposta;
import org.ong.pet.pex.backendpetx.dto.response.RespostaAnimalSemConjunto;
import org.ong.pet.pex.backendpetx.entities.Animal;
import org.ong.pet.pex.backendpetx.repositories.AnimalRepository;
import org.ong.pet.pex.backendpetx.service.AnimalService;
import org.ong.pet.pex.backendpetx.service.exceptions.AnimalJaCadastrado;
import org.ong.pet.pex.backendpetx.service.exceptions.AnimalNaoEncontrado;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.ong.pet.pex.backendpetx.service.impl.animalUtilService.ConversoresDeEntidade.converterParaAnimal;
import static org.ong.pet.pex.backendpetx.service.impl.animalUtilService.ConversoresDeEntidade.converterParaRespostaAnimalComConjuntoDTO;

@Service
public class AnimalServiceImpl implements AnimalService {

    private final AnimalRepository animalRepository;

    public AnimalServiceImpl(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    @Transactional
    public AnimalGenericoResposta cadastrarAnimalComConjunto(AnimalDTO animalDTO) {
        if (animalRepository.findAnimalByNome(animalDTO.getNome()) != null) {
            throw new AnimalJaCadastrado("Animal com o nome: " + animalDTO.getNome() + " já cadastrado");
        }
        if (animalDTO.getAnimalConjunto() != null) {
            animalDTO.getAnimalConjunto().forEach(conjunto -> {
                if (animalRepository.findAnimalByNome(conjunto.getNome()) != null) {
                    throw new AnimalJaCadastrado("Animal conjunto com o nome: " + conjunto.getNome() + " já cadastrado");
                }
                var a = conjunto.getAnimalConjunto();
            });
        }
        Animal animal = converterParaAnimal(animalDTO);

        if (animal.getAnimalConjunto() != null) {
            // vou salvar todos os conjuntos primieros
          animalRepository.saveAll(animal.getAnimalConjunto());
        }
        animalRepository.save(animal);
        return converterParaRespostaAnimalComConjuntoDTO(animal);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnimalDTO> animaisCadastrados() {
        return List.of();
    }

    @Override
    @Transactional
    public AnimalGenericoResposta atualizarAnimal(Long id, AnimalDTO animalSemConjuntoDTO) {
        try {
            Animal entidade = animalRepository.getReferenceById(id);

            entidade.setNome(animalSemConjuntoDTO.getNome());
            entidade.setRaca(animalSemConjuntoDTO.getRaca().toUpperCase());
            entidade.setIdade(animalSemConjuntoDTO.getIdade());
            entidade.setEspecieEnum(animalSemConjuntoDTO.getEspecie());
            entidade.setPorteEnum(animalSemConjuntoDTO.getPorte());
            entidade.setSexoEnum(animalSemConjuntoDTO.getSexo());
            entidade.setOrigemEnum(animalSemConjuntoDTO.getOrigem());
            entidade.setComportamentoEnum(animalSemConjuntoDTO.getComportamento());

            entidade = animalRepository.save(entidade);
            return converterParaRespostaAnimalComConjuntoDTO(entidade);

        } catch (EntityNotFoundException e) {
            throw new AnimalNaoEncontrado("Animal com id: " + id + " não encontrado");
        }
    }



    @Override
    @Transactional
    public void deletarPorId(Long id) {
        // Implementation here
    }

    @Override
    @Transactional
    public void declararObito(Long id) {
        // Implementation here
    }

    @Override
    @Transactional(readOnly = true)
    public RespostaAnimalSemConjunto animalSemConjunto(AnimalDTO animalSemConjuntoDTO) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public AnimalGenericoResposta buscarAnimalPorId(Long id) {
        Animal entidade = animalRepository.findById(id).orElseThrow(() -> new AnimalNaoEncontrado("Animal com id: " + id + " não encontrado"));
        return converterParaRespostaAnimalComConjuntoDTO(entidade);
    }
}
