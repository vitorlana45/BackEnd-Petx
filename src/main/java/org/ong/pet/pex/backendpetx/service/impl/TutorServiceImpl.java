package org.ong.pet.pex.backendpetx.service.impl;

import org.ong.pet.pex.backendpetx.dto.request.AtualizarTutorRequisicao;
import org.ong.pet.pex.backendpetx.dto.request.CadastrarTutorRequisicao;
import org.ong.pet.pex.backendpetx.dto.response.TutorDTOResposta;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    public TutorDTOResposta buscarTutorPorCpf(String cpf) {
        var tutor = tutorRepository.findTutorByCpf(cpf).orElseThrow(() -> TutorException.tutorNaoEncontrado(cpf));
        var animais = AnimalMapper.converterParaListaDeAnimaisComConjuntoDTO(tutor.getAnimais());

        return TutorDTOResposta.builder()
                .id(tutor.getId())
                .cpf(tutor.getCpf())
                .nome(tutor.getNome())
                .cep(tutor.getEndereco().getCep())
                .idade(tutor.getIdade())
                .telefone(tutor.getTelefone())
                .cidade(tutor.getEndereco().getCidade())
                .bairro(tutor.getEndereco().getBairro())
                .estado(tutor.getEndereco().getEstado())
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
                            .estado(cadastrarTutorRequisicao.estado())
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



//    @Override
//    @Transactional(readOnly = true)
//    public Page<TutorDTOResposta> paginarTutor(String nome, String cpf, String cidade, String bairro,
//                                               String rua, String telefone, String cep, Pageable pageable) {
//
////        // Construção da Specification usando o builder dinâmico
////        Specification<Tutor> especificacao = new ConstrutorDeEspecificacaoTutor()
////                .adicionarFiltroStringIniciais("nome", nome)
////                .adicionarFiltroStringIniciais("cpf", cpf)
////                .adicionarFiltroStringIniciais("endereco.cidade", cidade)
////                .adicionarFiltroStringIniciais("endereco.bairro", bairro)
////                .adicionarFiltroStringIniciais("endereco.rua", rua)
////                .build();
////
////        // Consulta paginada no repositório
////        Page<Tutor> page = tutorRepository.findAll(especificacao, pageable);
//
//        // Mapeamento dos resultados para DTOs
//
////        var dtos = page.stream()
////                .map(tutor -> TutorDTOResposta.builder()
////                        .id(tutor.getId())
////                        .cpf(tutor.getCpf())
////                        .nome(tutor.getNome())
////                        .idade(tutor.getIdade())
////                        .cep(tutor.getEndereco().getCep())
////                        .telefone(tutor.getTelefone())
////                        .cidade(tutor.getEndereco().getCidade())
////                        .bairro(tutor.getEndereco().getBairro())
////                        .estado(tutor.getEndereco().getEstado())
////                        .rua(tutor.getEndereco().getRua())
////                        .listaDeAnimais(AnimalMapper.converterParaListaDeAnimaisComConjuntoDTO(tutor.getAnimais()))
////                        .build())
////                .collect(Collectors.toList());
//
//        var dados = tutorRepository.findAll(pageable);
//
//        // Retorno da página final com os DTOs
//        return new PageImpl<>(null, pageable, null);
//
//
//    }

    @Override
    public Page<TutorDTOResposta> findAllTutorPaginacao(String nome, String cep, String cidade, String estado, Integer idade, Pageable pageable) {

        var tutores = tutorRepository.findAllTutorPorFiltro(nome, cep, cidade, estado, idade, pageable);

        return new PageImpl<>(tutores.stream().map(tutor -> TutorDTOResposta.builder()
                .id(tutor.getId())
                .cpf(tutor.getCpf())
                .nome(tutor.getNome())
                .idade(tutor.getIdade())
                .cep(tutor.getEndereco().getCep())
                .telefone(tutor.getTelefone())
                .cidade(tutor.getEndereco().getCidade())
                .bairro(tutor.getEndereco().getBairro())
                .estado(tutor.getEndereco().getEstado())
                .rua(tutor.getEndereco().getRua())
                .listaDeAnimais(AnimalMapper.converterParaListaDeAnimaisComConjuntoDTO(tutor.getAnimais()))
                .build()).collect(Collectors.toList()), pageable,tutores.getTotalElements());
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
        if (campoAtivo(att.estado())) tutor.getEndereco().setEstado(att.estado());
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

}