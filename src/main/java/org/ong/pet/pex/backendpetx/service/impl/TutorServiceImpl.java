package org.ong.pet.pex.backendpetx.service.impl;

import org.ong.pet.pex.backendpetx.dto.request.CadastrarTutorRequisicao;
import org.ong.pet.pex.backendpetx.dto.response.TutorDTOResponse;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
                            .cep(cadastrarTutorRequisicao.cep())
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

    @Transactional(readOnly = true)
    public Set<TutorDTOResponse> buscarTodosTutores() {

        List<TutorDTOResponse> listContendoTutoresRepetidos = new ArrayList<>();

        tutorRepository.findAll().forEach(tutor -> {
            listContendoTutoresRepetidos.add(TutorDTOResponse.builder()
                    .cpf(tutor.getCpf())
                    .nome(tutor.getNome())
                    .idade(tutor.getIdade())
                    .cep(tutor.getEndereco().getCep())
                    .telefone(tutor.getTelefone())
                    .cidade(tutor.getEndereco().getCidade())
                    .bairro(tutor.getEndereco().getBairro())
                    .rua(tutor.getEndereco().getRua())
                    .chipAnimal(tutor.getAnimais().stream()
                            .map(Animal::getChipId)
                            .collect(Collectors.toSet()))
                    .build()
            );
        });

        if(listContendoTutoresRepetidos.isEmpty())
            TutorException.naoHaTutoresCadastrados();


        return new HashSet<>(listContendoTutoresRepetidos);
    }

    @Transactional(readOnly = true)
    public TutorDTOResponse buscarTutorPorCpf(String cpf) {
     return converterParaTutorDTO(tutorRepository.findTutorByCpf(cpf).orElseThrow(() -> TutorException.tutorNaoEncontrado(cpf)));
    }

    @Transactional
    public void atualizarDadosTutor(String cpf, CadastrarTutorRequisicao cadastrarTutorRequisicao) {
        // Method implementation
    }

    @Transactional
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

    private TutorDTOResponse converterParaTutorDTO (Tutor tutor) {
        return new TutorDTOResponse(
                tutor.getCpf(),
                tutor.getNome(),
                tutor.getEndereco().getCep(),
                tutor.getIdade(),
                tutor.getTelefone(),
                tutor.getEndereco().getCidade(),
                tutor.getEndereco().getBairro(),
                tutor.getEndereco().getRua(),
                tutor.getAnimais().stream().map(Animal::getChipId).collect(Collectors.toSet())
        );
    }

}