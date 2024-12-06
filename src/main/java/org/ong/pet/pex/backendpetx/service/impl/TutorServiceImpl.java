package org.ong.pet.pex.backendpetx.service.impl;

import org.ong.pet.pex.backendpetx.dto.request.AtualizarTutorRequisicao;
import org.ong.pet.pex.backendpetx.dto.request.CadastrarTutorRequisicao;
import org.ong.pet.pex.backendpetx.dto.response.AnimalGenericoResposta;
import org.ong.pet.pex.backendpetx.dto.response.TutorDTOResponse;
import org.ong.pet.pex.backendpetx.entities.Animal;
import org.ong.pet.pex.backendpetx.entities.Tutor;
import org.ong.pet.pex.backendpetx.entities.incorporarEntidades.Endereco;
import org.ong.pet.pex.backendpetx.repositories.AnimalRepository;
import org.ong.pet.pex.backendpetx.repositories.TutorRepository;
import org.ong.pet.pex.backendpetx.service.TutorService;
import org.ong.pet.pex.backendpetx.service.exceptions.PetXException;
import org.ong.pet.pex.backendpetx.service.exceptions.TutorException;
import org.ong.pet.pex.backendpetx.service.impl.serviceUtils.AnimalUtils;
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
    private final AnimalRepository animalRepository;
    private final AnimalUtils animalUtils;

    public TutorServiceImpl(TutorRepository tutorRepository, AnimalRepository animalRepository, AnimalUtils animalUtils) {
        this.tutorRepository = tutorRepository;
        this.animalRepository = animalRepository;
        this.animalUtils = animalUtils;
    }

    @Transactional(readOnly = true)
    public TutorDTOResponse buscarTutorPorCpf(String cpf) {
        var tutor = tutorRepository.findTutorByCpf(cpf).orElseThrow(() -> TutorException.tutorNaoEncontrado(cpf));
        Set<AnimalGenericoResposta> todosOsAnimals = tutor.getAnimais().stream()
                .map(animal -> animalUtils.buscarAnimalPorIdComConjuntoResposta(animal.getId()))
                .collect(Collectors.toSet());

        return TutorDTOResponse.builder()
                .id(tutor.getId())
                .cpf(tutor.getCpf())
                .nome(tutor.getNome())
                .cep(tutor.getEndereco().getCep())
                .idade(tutor.getIdade())
                .telefone(tutor.getTelefone())
                .cidade(tutor.getEndereco().getCidade())
                .bairro(tutor.getEndereco().getBairro())
                .rua(tutor.getEndereco().getRua())
                .listaDeAnimais(todosOsAnimals)
                .build();
    }

    @Transactional
    public Long cadastrarTutor(CadastrarTutorRequisicao cadastrarTutorRequisicao) {
        if (cadastrarTutorRequisicao.cpf() == null || cadastrarTutorRequisicao.cpf().isEmpty()) {
            throw TutorException.cpfNaoPodeSerVazioOuNulo();
        }

        Animal pet = animalRepository.findAnimalByChipId(cadastrarTutorRequisicao.chipAnimal())
                .orElseThrow(() -> PetXException.animalNaoEncontrado(cadastrarTutorRequisicao.chipAnimal()));

        Optional<Tutor> encontrarTutor = tutorRepository.findTutorByCpf(cadastrarTutorRequisicao.cpf());

        Tutor tutorRecipiente = encontrarTutor.map(tutor -> {
            verificarSeTutorJaTemEsteAnimal(pet, tutor);
            tutor.getAnimais().add(pet);
            pet.getTutores().add(tutor);
            return tutorRepository.save(tutor);
        }).orElseGet(() -> {
            Tutor novoTutor = Tutor.builder()
                    .idade(cadastrarTutorRequisicao.idade())
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

    @Transactional
    public void deletarTutor(String cpf) {
        tutorRepository.findTutorByCpf(cpf).ifPresentOrElse(tutor -> {
            tutor.setOng(null);
            tutor.getAnimais().forEach(animal -> animal.getTutores().remove(tutor));
            tutorRepository.deleteById(tutor.getId());
        }, () -> {
            throw TutorException.tutorNaoEncontrado(cpf);
        });
    }

    @Transactional(readOnly = true)
    public Set<TutorDTOResponse> buscarTodosTutores() {
        List<TutorDTOResponse> listContendoTutoresRepetidos = new ArrayList<>();

        tutorRepository.findAll().forEach(tutor -> {
            var listAnimals = new ArrayList<>(tutor.getAnimais());
            listContendoTutoresRepetidos.add(TutorDTOResponse.builder()
                    .id(tutor.getId())
                    .cpf(tutor.getCpf())
                    .nome(tutor.getNome())
                    .idade(tutor.getIdade())
                    .cep(tutor.getEndereco().getCep())
                    .telefone(tutor.getTelefone())
                    .cidade(tutor.getEndereco().getCidade())
                    .bairro(tutor.getEndereco().getBairro())
                    .rua(tutor.getEndereco().getRua())
                    .listaDeAnimais(listAnimals.stream()
                            .map(animal -> animalUtils.buscarAnimalPorIdComConjuntoResposta(animal.getId()))
                            .collect(Collectors.toSet()))
                    .build());
        });

        if (listContendoTutoresRepetidos.isEmpty()) TutorException.naoHaTutoresCadastrados();

        return new HashSet<>(listContendoTutoresRepetidos);
    }

    @Transactional
    public void deletarTutorPorCpf(String cpf) {
        var existeTutor = tutorRepository.findTutorByCpf(cpf).orElseThrow(() -> TutorException.tutorNaoEncontrado(cpf));
        existeTutor.setOng(null);
        existeTutor.getAnimais().forEach(animal -> animal.getTutores().remove(existeTutor));
        tutorRepository.deleteById(existeTutor.getId());
    }

    @Transactional
    public String atualizarDadosTutor(String cpfAntigo, AtualizarTutorRequisicao att) {
        Tutor verificandoTutorExiste = tutorRepository.findTutorByCpf(cpfAntigo).orElseThrow(() -> TutorException.tutorNaoEncontrado(att.cpf()));

        if (tutorRepository.existsByCpf(att.cpf()))
            throw TutorException.jaExiste("ja existe um tutor com este", "CPF", att.cpf());

        if(att.cpf() != null && !att.cpf().isEmpty()) {
            verificandoTutorExiste.setCpf(att.cpf());
        }

        if(att.nome() != null && !att.nome().isEmpty()) {
            verificandoTutorExiste.setNome(att.nome());
        }

        if(att.telefone() != null && !att.telefone().isEmpty()) {
            verificandoTutorExiste.setTelefone(att.telefone());
        }

        if(att.idade() != null) verificandoTutorExiste.setIdade(att.idade());
        if(att.cidade() != null && !att.cidade().isEmpty()) verificandoTutorExiste.getEndereco().setCidade(att.cidade());
        if(att.bairro() != null && !att.bairro().isEmpty()) verificandoTutorExiste.getEndereco().setBairro(att.bairro());
        if(att.rua() != null && !att.rua().isEmpty()) verificandoTutorExiste.getEndereco().setRua(att.rua());
        if(att.cep() != null && !att.cep().isEmpty()) verificandoTutorExiste.getEndereco().setCep(att.cep());

        var entidadeSalva = tutorRepository.saveAndFlush(converterTutorDTOParaEntidade(verificandoTutorExiste, att));
        return entidadeSalva.getCpf();
    }

    private void verificarSeTutorJaTemEsteAnimal(Animal animal, Tutor tutor) {
        tutor.getAnimais().stream()
                .filter(animalTutor -> Objects.equals(animalTutor.getChipId(), animal.getChipId()))
                .findAny()
                .ifPresent(animalTutor -> {
                    throw TutorException.jaExiste("Este Animal já pertence a este tutor", "CPF", tutor.getCpf());
                });
    }

    private Tutor converterTutorDTOParaEntidade(Tutor tutor, AtualizarTutorRequisicao att) {
        tutor.setCpf(att.cpf());
        tutor.setNome(att.nome());
        tutor.setTelefone(att.telefone());
        tutor.setIdade(att.idade());
        tutor.setEndereco(Endereco.builder()
                .cidade(att.cidade())
                .bairro(att.bairro())
                .rua(att.rua())
                .cep(att.cep())
                .build());
        return tutor;
    }
}