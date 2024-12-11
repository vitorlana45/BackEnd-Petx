package org.ong.pet.pex.backendpetx.service.impl;

import org.ong.pet.pex.backendpetx.dto.request.AtualizarTutorRequisicao;
import org.ong.pet.pex.backendpetx.dto.request.CadastrarTutorRequisicao;
import org.ong.pet.pex.backendpetx.dto.response.AnimalGenericoResposta;
import org.ong.pet.pex.backendpetx.dto.response.TutorDTOResponse;
import org.ong.pet.pex.backendpetx.entities.Animal;
import org.ong.pet.pex.backendpetx.entities.Tutor;
import org.ong.pet.pex.backendpetx.entities.incorporarEntidades.Endereco;
import org.ong.pet.pex.backendpetx.enums.ComportamentoEnum;
import org.ong.pet.pex.backendpetx.enums.EspecieEnum;
import org.ong.pet.pex.backendpetx.enums.MaturidadeEnum;
import org.ong.pet.pex.backendpetx.enums.OrigemAnimalEnum;
import org.ong.pet.pex.backendpetx.enums.PorteEnum;
import org.ong.pet.pex.backendpetx.enums.SexoEnum;
import org.ong.pet.pex.backendpetx.enums.StatusEnum;
import org.ong.pet.pex.backendpetx.repositories.AnimalConjuntoRepository;
import org.ong.pet.pex.backendpetx.repositories.AnimalRepository;
import org.ong.pet.pex.backendpetx.repositories.TutorRepository;
import org.ong.pet.pex.backendpetx.repositories.UsuarioRepository;
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
    private final UsuarioRepository usuarioRepository;
    private final AnimalConjuntoRepository animalConjuntoRepository;

    public TutorServiceImpl(TutorRepository tutorRepository, AnimalRepository animalRepository, AnimalUtils animalUtils, UsuarioRepository usuarioRepository, AnimalConjuntoRepository animalConjuntoRepository) {
        this.tutorRepository = tutorRepository;
        this.animalRepository = animalRepository;
        this.animalUtils = animalUtils;
        this.usuarioRepository = usuarioRepository;
        this.animalConjuntoRepository = animalConjuntoRepository;
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

        // Busca o animal principal pelo chip
        Animal pet = animalRepository.findAnimalByChipId(cadastrarTutorRequisicao.chipAnimal())
                .orElseThrow(() -> PetXException.animalNaoEncontrado(cadastrarTutorRequisicao.chipAnimal()));

        // Busca todos os animais associados ao animal principal
        Set<Animal> animais = animalUtils.buscarAnimalPorIdComConjuntoEntidade(pet.getId());

        // me retornou todos associados ao principal que me mandou mas nao me retornou o principal
        if(animais != null && !animais.isEmpty()) {
            animais.add(pet);
        } else {
            animais = new HashSet<>();
            animais.add(pet);
        }

        Optional<Tutor> encontrarTutor = tutorRepository.findTutorByCpf(cadastrarTutorRequisicao.cpf());

        Set<Animal> animaisFinal = new HashSet<>(animais);

        Tutor tutorRecipiente = encontrarTutor.map(tutor -> {
            // Verifica se o tutor já tem algum dos animais
            animaisFinal.forEach(animal -> verificarSeTutorJaTemEsteAnimal(animal, tutor));

            // Adiciona os animais ao tutor e o tutor ao animal
            tutor.getAnimais().addAll(animaisFinal);
            animaisFinal.forEach(animal -> animal.getTutores().add(tutor));

            // Salva o tutor e os animais
            tutorRepository.save(tutor);
            animalRepository.saveAllAndFlush(animaisFinal); // Salva todos os animais associados

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
                    .ong(pet.getOng())
                    .animais(animaisFinal) // Garante que estamos passando uma lista sem duplicados
                    .build();

            // Adiciona o tutor aos animais
            animaisFinal.forEach(animal -> animal.getTutores().add(novoTutor));
            novoTutor.getAnimais().addAll(animaisFinal);

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

    //TODO: remover o animalRetido
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
                            .doencas(animal.getDoencas())  // Assumindo que animal.getDoencas() é um List<String>
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
                    .listaDeAnimais(listaDeAnimais)  // A lista de animais única
                    .build());
        });

        if (listContendoTutoresRepetidos.isEmpty()) {
            TutorException.naoHaTutoresCadastrados();
        }

        return listContendoTutoresRepetidos;
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
        Tutor verificandoTutorExiste = tutorRepository.findTutorByCpf(cpfUrl).orElseThrow(() -> TutorException.tutorNaoEncontrado(cpfUrl));

        if (!cpfUrl.equals(att.cpf())) {
            if (tutorRepository.findTutorByCpf(att.cpf()).isPresent())
                throw TutorException.jaExiste("ja existe um tutor com este", "CPF", att.cpf());
            else {
                verificandoTutorExiste.setCpf(att.cpf());
            }
        }

        if (att.nome() != null && !att.nome().isEmpty()) {
            verificandoTutorExiste.setNome(att.nome());
        }

        if (att.telefone() != null && !att.telefone().isEmpty()) {
            verificandoTutorExiste.setTelefone(att.telefone());
        }

        if (att.idade() != null) verificandoTutorExiste.setIdade(att.idade());
        if (att.cidade() != null && !att.cidade().isEmpty())
            verificandoTutorExiste.getEndereco().setCidade(att.cidade());
        if (att.bairro() != null && !att.bairro().isEmpty())
            verificandoTutorExiste.getEndereco().setBairro(att.bairro());
        if (att.rua() != null && !att.rua().isEmpty()) verificandoTutorExiste.getEndereco().setRua(att.rua());
        if (att.cep() != null && !att.cep().isEmpty()) verificandoTutorExiste.getEndereco().setCep(att.cep());

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