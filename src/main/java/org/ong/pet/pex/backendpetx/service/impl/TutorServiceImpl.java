package org.ong.pet.pex.backendpetx.service.impl;

import org.ong.pet.pex.backendpetx.dto.request.AtualizarTutorRequisicao;
import org.ong.pet.pex.backendpetx.dto.request.CadastrarTutorRequisicao;
import org.ong.pet.pex.backendpetx.dto.response.AnimalGenericoResposta;
import org.ong.pet.pex.backendpetx.dto.response.TutorDTOResponse;
import org.ong.pet.pex.backendpetx.entities.Animal;
import org.ong.pet.pex.backendpetx.entities.Tutor;
import org.ong.pet.pex.backendpetx.entities.incorporarEntidades.Endereco;
import org.ong.pet.pex.backendpetx.repositories.AnimalConjuntoRepository;
import org.ong.pet.pex.backendpetx.repositories.AnimalRepository;
import org.ong.pet.pex.backendpetx.repositories.TutorRepository;
import org.ong.pet.pex.backendpetx.service.TutorService;
import org.ong.pet.pex.backendpetx.service.exceptions.PetXException;
import org.ong.pet.pex.backendpetx.service.exceptions.TutorException;
import org.ong.pet.pex.backendpetx.service.impl.serviceUtils.AnimalUtils;
import org.ong.pet.pex.backendpetx.service.mappers.AnimalMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final AnimalConjuntoRepository animalConjuntoRepository;
    private final AnimalMapper animalMapper;

    public TutorServiceImpl(TutorRepository tutorRepository, AnimalRepository animalRepository, AnimalUtils animalUtils, AnimalConjuntoRepository animalConjuntoRepository, AnimalMapper animalMapper) {
        this.tutorRepository = tutorRepository;
        this.animalRepository = animalRepository;
        this.animalUtils = animalUtils;
        this.animalConjuntoRepository = animalConjuntoRepository;
        this.animalMapper = animalMapper;
    }

    @Transactional(readOnly = true)
    public TutorDTOResponse buscarTutorPorCpf(String cpf) {
        var tutor = tutorRepository.findTutorByCpf(cpf).orElseThrow(() -> TutorException.tutorNaoEncontrado(cpf));
        var animais = AnimalMapper.converterParaListaDeAnimaisComConjuntoDTO(tutor.getAnimais());

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
                .listaDeAnimais(animais)
                .build();
    }

    @Transactional
    public Long cadastrarTutor(CadastrarTutorRequisicao cadastrarTutorRequisicao) {

        if (cadastrarTutorRequisicao.cpf() == null || cadastrarTutorRequisicao.cpf().isEmpty()) {
            throw TutorException.cpfNaoPodeSerVazioOuNulo();
        }

        // Verifica se todos os animais da lista de chips existem
        List<Animal> pets = cadastrarTutorRequisicao.animalChips().stream()
                .map(chip -> animalRepository.findAnimalByChipId(chip)
                        .orElseThrow(() -> PetXException.animalNaoEncontrado(chip)))
                .toList();

        // Busca os animais e suas associações
        Set<Animal> animais = new HashSet<>(pets);
        pets.forEach(animal -> {
            var conjuntoAnimais = animalConjuntoRepository.findByAnimalPrincipalIdOrAnimalRelacionamentoId(animal.getId());
            conjuntoAnimais.forEach(animalConjunto -> {
                Animal animalRelacionado = animalRepository.findById(animalConjunto.getAnimalRelacionamento().getId())
                        .orElseThrow(() -> PetXException.animalNaoEncontrado(animalConjunto.getAnimalRelacionamento().getId().toString()));
                animais.add(animalRelacionado);
            });
        });

        Optional<Tutor> encontrarTutor = tutorRepository.findTutorByCpf(cadastrarTutorRequisicao.cpf());

        Tutor tutorRecipiente = encontrarTutor.map(tutor -> {

            animais.forEach(animal -> verificarSeTutorJaTemEsteAnimal(animal, tutor));

            tutor.getAnimais().addAll(animais);
            animais.forEach(animal -> animal.getTutores().add(tutor));

            tutorRepository.save(tutor);
            animalRepository.saveAllAndFlush(animais);

            return tutor;
        }).orElseGet(() -> {
            // Cria um novo tutor
            Tutor novoTutor = Tutor.builder()
                    .idade(cadastrarTutorRequisicao.idade())
                    .cpf(cadastrarTutorRequisicao.cpf())
                    .nome(cadastrarTutorRequisicao.nome())
                    .telefone(cadastrarTutorRequisicao.telefone())
                    .endereco(Endereco.builder()
                            .cidade(cadastrarTutorRequisicao.cidade())
                            .bairro(cadastrarTutorRequisicao.bairro())
                            .rua(cadastrarTutorRequisicao.rua())
                            .cep(cadastrarTutorRequisicao.cep())
                            .build())
                    .ong(pets.getFirst().getOng())
                    .animais(animais)
                    .build();

            // Adiciona o tutor aos animais
            animais.forEach(animal -> animal.getTutores().add(novoTutor));
            novoTutor.getAnimais().addAll(animais);

            // Salva o novo tutor
            tutorRepository.save(novoTutor);

            return novoTutor;
        });

        return tutorRecipiente.getId();
    }


    @Transactional
    public void deletarTutorPorId(Long id) {
        tutorRepository.findById(id).ifPresentOrElse(tutor -> {
            tutor.setOng(null);
            tutor.getAnimais().forEach(animal -> animal.getTutores().remove(tutor));
            tutorRepository.deleteById(tutor.getId());
        }, () -> {
            throw TutorException.tutorNaoEncontrado(id.toString());
        });
    }


    @Transactional(readOnly = true)
    public Set<TutorDTOResponse> buscarTodosTutores() {
        Set<TutorDTOResponse> listContendoTutoresRepetidos = new HashSet<>();

        tutorRepository.findAll().forEach(tutor -> {

            Set<AnimalGenericoResposta> listaDeAnimais = tutor.getAnimais().stream()
                    .map(animal -> AnimalGenericoResposta.builder()
                            .id(animal.getId())
                            .chipId(animal.getChipId())
                            .nome(animal.getNome())
                            .raca(animal.getRaca())
                            .maturidade(String.valueOf(animal.getMaturidadeEnum()))
                            .sexo(String.valueOf(animal.getSexoEnum()))
                            .origem(String.valueOf(animal.getOrigemEnum()))
                            .porte(String.valueOf(animal.getPorteEnum()))
                            .comportamento(String.valueOf(animal.getComportamentoEnum()))
                            .especie(String.valueOf(animal.getEspecieEnum()))
                            .doencas(animal.getDoencas())
                            .status(animal.getStatusEnum().getStatus())
                            .build())
                    .collect(Collectors.toSet());

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
                    .listaDeAnimais(listaDeAnimais)
                    .build());
        });

        if (listContendoTutoresRepetidos.isEmpty()) {
            TutorException.naoHaTutoresCadastrados();
        }

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
    public String atualizarDadosTutor(String cpfUrl, AtualizarTutorRequisicao att) {
        Tutor tutor = tutorRepository.findTutorByCpf(cpfUrl)
                .orElseThrow(() -> TutorException.tutorNaoEncontrado(cpfUrl));

        validarAtualizacaoCpf(cpfUrl, att, tutor);
        atualizarCamposPessoais(att, tutor);
        atualizarEndereco(att, tutor);
        atualizarAnimaisAssociados(att, tutor);

        tutorRepository.saveAndFlush(tutor);
        return tutor.getCpf();
    }

    private void validarAtualizacaoCpf(String cpfUrl, AtualizarTutorRequisicao att, Tutor tutor) {
        if (!cpfUrl.equals(att.cpf())) {
            if (tutorRepository.findTutorByCpf(att.cpf()).isPresent()) {
                throw TutorException.jaExiste("ja existe um tutor com este", "CPF", att.cpf());
            }
            tutor.setCpf(att.cpf());
        }
    }

    private void atualizarCamposPessoais(AtualizarTutorRequisicao att, Tutor tutor) {
        if (campoAtivo(att.nome())) tutor.setNome(att.nome());
        if (campoAtivo(att.telefone())) tutor.setTelefone(att.telefone());
        if (att.idade() != null) tutor.setIdade(att.idade());
    }

    private void atualizarEndereco(AtualizarTutorRequisicao att, Tutor tutor) {
        if (campoAtivo(att.cidade())) tutor.getEndereco().setCidade(att.cidade());
        if (campoAtivo(att.bairro())) tutor.getEndereco().setBairro(att.bairro());
        if (campoAtivo(att.rua())) tutor.getEndereco().setRua(att.rua());
        if (campoAtivo(att.cep())) tutor.getEndereco().setCep(att.cep());
    }

    private void atualizarAnimaisAssociados(AtualizarTutorRequisicao att, Tutor tutor) {
        if (att.chipsAnimais() == null || att.chipsAnimais().isEmpty()) return;

        Set<Animal> animaisAtualizados = att.chipsAnimais().stream()
                .map(chip -> animalRepository.findAnimalByChipId(chip)
                        .orElseThrow(() -> PetXException.animalNaoEncontrado(chip)))
                .collect(Collectors.toSet());

        animaisAtualizados.forEach(animal -> verificarSeTutorJaTemEsteAnimal(animal, tutor));

        tutor.getAnimais().forEach(animal -> animal.getTutores().remove(tutor));
        tutor.getAnimais().clear();
        tutor.getAnimais().addAll(animaisAtualizados);
        animaisAtualizados.forEach(animal -> animal.getTutores().add(tutor));

        animalRepository.saveAll(animaisAtualizados);
    }

    private boolean campoAtivo(String campo) {
        return campo != null && !campo.isEmpty();
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