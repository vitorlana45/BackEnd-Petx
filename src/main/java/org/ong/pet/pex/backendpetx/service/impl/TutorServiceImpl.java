package org.ong.pet.pex.backendpetx.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.ong.pet.pex.backendpetx.dto.request.CadastrarTutorRequisicao;
import org.ong.pet.pex.backendpetx.entities.Animal;
import org.ong.pet.pex.backendpetx.entities.Tutor;
import org.ong.pet.pex.backendpetx.entities.incorporarEntidades.Endereco;
import org.ong.pet.pex.backendpetx.repositories.AnimalRepository;
import org.ong.pet.pex.backendpetx.repositories.OngRepository;
import org.ong.pet.pex.backendpetx.repositories.TutorRepository;
import org.ong.pet.pex.backendpetx.service.TutorService;
import org.ong.pet.pex.backendpetx.service.exceptions.PetXException;
import org.ong.pet.pex.backendpetx.service.exceptions.TutorException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class TutorServiceImpl implements TutorService {

    private final TutorRepository tutorRepository;
    private final OngRepository ongRepository;
    private final AnimalRepository animalRepository;

    public TutorServiceImpl(TutorRepository tutorRepository, OngRepository ongRepository, AnimalRepository animalRepository) {
        this.tutorRepository = tutorRepository;
        this.ongRepository = ongRepository;
        this.animalRepository = animalRepository;
    }

    @Transactional
    public Long cadastrarTutor(CadastrarTutorRequisicao cadastrarTutorRequisicao) {

        if (cadastrarTutorRequisicao.cpf() == null || cadastrarTutorRequisicao.cpf().isEmpty()) {
            throw TutorException.cpfNaoPodeSerVazioOuNulo();
        }

        Animal pet = animalRepository.findAnimalByChipIdAndEstaVivo(cadastrarTutorRequisicao.chipAnimal(), true)
                .orElseThrow(() -> PetXException.animalJaFalecido(cadastrarTutorRequisicao.chipAnimal()));

        Optional<Tutor> encontrarTutor = tutorRepository.findTutorByCpf(cadastrarTutorRequisicao.cpf());

        Tutor tutorRecipiente = encontrarTutor.map(tutor -> {
            verificarSeTutorJaTemEsteAnimal(pet, tutor);
            tutor.getAnimais().add(pet);
            pet.getTutores().add(tutor);
            return tutorRepository.save(tutor); // Salvar tutor com relação atualizada
        }).orElseGet(() -> {
            Tutor novoTutor = Tutor.builder()
                    .cpf(cadastrarTutorRequisicao.cpf())
                    .nome(cadastrarTutorRequisicao.nome())
                    .telefone(cadastrarTutorRequisicao.telefone())
                    .idade(cadastrarTutorRequisicao.idade())
                    .endereco(Endereco.builder()
                            .cidade(cadastrarTutorRequisicao.cidade())
                            .bairro(cadastrarTutorRequisicao.bairro())
                            .rua(cadastrarTutorRequisicao.rua())
                            .build())
                    .ong(pet.getOng())
                    .animais(Set.of(pet))
                    .build();
            pet.getTutores().add(novoTutor);
            return tutorRepository.save(novoTutor);
        });
        return tutorRecipiente.getId();
    }

    public void deletarTutor(String cpf) {

    }

    public void buscarTodosTutores() {
        // Method implementation
    }

    public void buscarTutorPorCpf(String cpf) {
        // Method implementation
    }

    public void atualizarDadosTutor(String cpf, CadastrarTutorRequisicao cadastrarTutorRequisicao) {
        // Method implementation
    }

    public void deletarTutorPorCpf(String cpf) {
        // Method implementation
    }

    private void verificarSeTutorJaTemEsteAnimal(Animal animal, Tutor tutor) {
        tutor.getAnimais().stream()
                .filter(animalTutor -> Objects.equals(animalTutor.getChipId(), animal.getChipId()))
                .findAny()
                .ifPresent(animalTutor -> {
                    throw TutorException.jaExiste("Este Animal já pertence a este tutor", "CPF", tutor.getCpf());
                });
    }
}