package org.ong.pet.pex.backendpetx.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.ong.pet.pex.backendpetx.dto.request.AnimalDTO;
import org.ong.pet.pex.backendpetx.dto.response.AnimalGenericoResposta;
import org.ong.pet.pex.backendpetx.dto.response.RespostaAnimalSemConjunto;
import org.ong.pet.pex.backendpetx.entities.Animal;
import org.ong.pet.pex.backendpetx.repositories.AnimalRepository;
import org.ong.pet.pex.backendpetx.service.AnimalService;
import org.ong.pet.pex.backendpetx.service.exceptions.AnimalJaCadastrado;
import org.ong.pet.pex.backendpetx.service.exceptions.AnimalNaoEncontrado;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

import static org.ong.pet.pex.backendpetx.service.impl.animalUtilService.ConversoresDeEntidade.converterParaAnimal;
import static org.ong.pet.pex.backendpetx.service.impl.animalUtilService.ConversoresDeEntidade.converterParaRespostaAnimalComConjuntoDTO;

@Service
@SuppressWarnings("all")
public class AnimalServiceImpl implements AnimalService {

    private final AnimalRepository animalRepository;
    private static final Logger logger = LoggerFactory.getLogger(AnimalServiceImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    public AnimalServiceImpl(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    @Transactional
    public AnimalGenericoResposta cadastrarAnimalComConjunto(AnimalDTO animalDTO) {

        logger.info("Iniciando criação de um novo animal com nome: {}", animalDTO.getNome());

        if (animalRepository.findAnimalByNome(animalDTO.getNome()) != null) {
            throw new AnimalJaCadastrado("Animal com o nome: " + animalDTO.getNome() + " já cadastrado");
        }
        if (animalDTO.getAnimalConjunto() != null) {
            animalDTO.getAnimalConjunto().forEach(conjunto -> {
                if (animalRepository.findAnimalByNome(conjunto.getNome()) != null) {
                    throw new AnimalJaCadastrado("Animal conjunto com o nome: " + conjunto.getNome() + " já cadastrado");
                }
            });
        }
        Animal animal = converterParaAnimal(animalDTO);
         animal.getAnimalConjunto().addAll(new HashSet<>());

        animalRepository.save(animal);

        if (animal.getAnimalConjunto() != null) {
            logger.info("Salvando animais do conjunto antes de salvar o animal Principal {}", animal.getAnimalConjunto());
            animalRepository.saveAll(animal.getAnimalConjunto());
        }
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
        try {
            // 1. pegar o animal pelo id
            Animal animalParaDeletar = animalRepository.findById(id)
                    .orElseThrow(() -> new AnimalNaoEncontrado("Animal com id: " + id + " não encontrado"));

            // 2. fazer uma query personalizada para buscar todos os animais que tem o animalFilho que quero deletar como pai
            String queryBuscarPais = "SELECT a FROM Animal a JOIN a.animalConjunto ac WHERE ac.id = :animalId";
            List<Animal> animaisPais = entityManager.createQuery(queryBuscarPais, Animal.class)
                    .setParameter("animalId", id)
                    .getResultList();

            // 3. removendo todos as referencias ligadas ao animal que quero deletar
            for (Animal todosOsAnimaisLigadoAoQueQueroDeletar : animaisPais) {
                todosOsAnimaisLigadoAoQueQueroDeletar.getAnimalConjunto().removeIf(animal -> animal.getId().equals(id));
                animalRepository.save(todosOsAnimaisLigadoAoQueQueroDeletar);
            }

            // 3. Limpar o próprio conjunto do animal (onde ele é pai)
//            pego a lista de conjunto dele e limpo ela para nao ter nenhuma referência
            if (animalParaDeletar.getAnimalConjunto() != null) {
                animalParaDeletar.getAnimalConjunto().clear();
                animalRepository.save(animalParaDeletar);
            }

            // 4. Limpar a tabela de junção manualmente para garantir
//            tenho que remover todos os registros que tem o id do animal que quero deletar
            String deletarRelacoes = "DELETE FROM animal_conjunto_adocao WHERE animal_conjunto_id = :id OR animal_id = :id";
            entityManager.createNativeQuery(deletarRelacoes)
                    .setParameter("id", id)
                    .executeUpdate();

            // 5. Deletar o animal
            animalRepository.delete(animalParaDeletar);

        } catch (Exception e) {
            throw new AnimalNaoEncontrado("Erro ao tentar excluir o animal com id: " + id + " - " + e.getMessage());
        }
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
