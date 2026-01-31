package org.ong.pet.pex.backendpetx.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.ong.pet.pex.backendpetx.controllers.exceptions.setup.AppException;
import org.ong.pet.pex.backendpetx.dto.request.AnimalGenericoRequisicao;
import org.ong.pet.pex.backendpetx.dto.request.AnimalObituarioResquisicao;
import org.ong.pet.pex.backendpetx.dto.response.AnimalGenericoResposta;
import org.ong.pet.pex.backendpetx.dto.response.AnimalPaginadoResposta;
import org.ong.pet.pex.backendpetx.entities.*;
import org.ong.pet.pex.backendpetx.entities.media.MediaTargetType;
import org.ong.pet.pex.backendpetx.enums.*;
import org.ong.pet.pex.backendpetx.repositories.*;
import org.ong.pet.pex.backendpetx.repositories.specifcs.AnimalSpecs;
import org.ong.pet.pex.backendpetx.security.utils.SecurityUtils;
import org.ong.pet.pex.backendpetx.service.AnimalService;
import org.ong.pet.pex.backendpetx.service.Minio;
import org.ong.pet.pex.backendpetx.service.exceptions.PetXException;
import org.ong.pet.pex.backendpetx.service.impl.serviceUtils.AnimalUtils;
import org.ong.pet.pex.backendpetx.service.mappers.AnimalMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.ong.pet.pex.backendpetx.service.mappers.AnimalMapper.converterParaRespostaAnimalComConjuntoDTO;

@Service
@Transactional
public class AnimalServiceImpl implements AnimalService {

    private final AnimalRepository animalRepository;
    // Mantido para futuras operações (ex: vincular ONG), atualmente não utilizado
    @SuppressWarnings("unused")
    private final OngRepository ongRepository;
    private final AnimalConjuntoRepository animalConjuntoRepository;
    private final TutorRepository tutorRepository;
    private final ObitoRepository obitoRepository;
    private static final Logger logger = LoggerFactory.getLogger(AnimalServiceImpl.class);
    private final AnimalUtils animalUtils;
    private final Minio minioService;
    private final LogAtividadeService logAtividadeService ;
    private final MediaService mediaService;

    @Value("${minio.bucket:petx}")
    private String animalBucketName;

        public AnimalServiceImpl(AnimalRepository animalRepository, OngRepository ongRepository, AnimalConjuntoRepository animalConjuntoRepository, TutorRepository tutorRepository, ObitoRepository obitoRepository, AnimalUtils animalUtils, Minio minioService, LogAtividadeService logAtividadeService, MediaService mediaService) {
        this.animalRepository = animalRepository;
        this.ongRepository = ongRepository;
        this.animalConjuntoRepository = animalConjuntoRepository;
        this.tutorRepository = tutorRepository;
        this.obitoRepository = obitoRepository;
        this.animalUtils = animalUtils;
        this.minioService = minioService;
        this.logAtividadeService = logAtividadeService;
            this.mediaService = mediaService;
        }




    @Transactional
    public void adicionarAdocaoConjuntaEmAnimal(Map<String, Long> ids) {
        Long idPrincipal = ids.remove("principal");

        // Pegando e verificando a lista dos animais passados no mapa
        var lista = ids.values().stream()
                .map(value -> animalRepository.findAnimalById(value)
                        .orElseThrow(() -> PetXException.animalNaoEncontrado(value.toString())))
                .collect(Collectors.toSet());

        // Verifica se os animais não estão falecidos
        lista.forEach(this::verificarSeOAnimalNaoEstaFalecido);

        // Pegando o animal principal
        var animalPrincipal = animalRepository.findAnimalById(idPrincipal)
                .orElseThrow(() -> PetXException.animalNaoEncontrado(idPrincipal.toString()));

        verificarSeOAnimalNaoEstaFalecido(animalPrincipal);

        // Processando cada animal para associação
        lista.forEach(animal -> {
            // Verifica se o animal já está associado
            var animalConjunto = animalConjuntoRepository.findByAnimalRelacionamentoId(animal.getId());
            if (animalConjunto.isPresent()) {
                throw PetXException.animalJaCadastrado("Animal com ID: " + animal.getId() + " já pertence a um conjunto");
            }

            // Criando a entidade de associação
            AnimalConjunto entidade = new AnimalConjunto();
            entidade.setAnimalPrincipal(animalPrincipal); // Definindo o animal principal
            entidade.setAnimalRelacionamento(animal); // Definindo o animal associado

            // Associando tutores do animal principal
            var tutores = animalPrincipal.getTutores();
            if (tutores != null && !tutores.isEmpty()) {
                Set<Tutor> novosTutores = new HashSet<>(tutores);

                // Sincronizando a relação bidirecional
                novosTutores.forEach(tutor -> tutor.getAnimais().add(animal)); // Atualiza os animais no lado Tutor
                animal.setTutores(novosTutores);

                // Persistindo animal com novos tutores
                animalRepository.save(animal);

                // Persistindo os tutores atualizados
                tutorRepository.saveAll(novosTutores); // Certifique-se de ter este repositório configurado
            }

            // Salvando a associação no banco
            animalConjuntoRepository.saveAndFlush(entidade);
        });
    }

    @Override
    @Transactional
    public AnimalGenericoResposta atualizarAnimal(Long id, AnimalGenericoRequisicao animalSemConjuntoDTO) {
        try {
            Animal entidade = animalRepository.getReferenceById(id);
            var animalReq = animalSemConjuntoDTO.getChipId();
            System.out.println("chip da entidade em edicao " + animalReq);


            System.out.println("entidade encontrada " + entidade.getId());

            var existeAnimalComChipIdDaEdicao = animalRepository.findAnimalByChipId(animalSemConjuntoDTO.getChipId());

            if(existeAnimalComChipIdDaEdicao.isPresent() && !Objects.equals(entidade.getId(), existeAnimalComChipIdDaEdicao.get().getId())){
                throw AppException.chipDuplicado(animalSemConjuntoDTO.getChipId());
            }

            entidade.setChipId(animalSemConjuntoDTO.getChipId());
            entidade.setNome(animalSemConjuntoDTO.getNome());
            entidade.setRaca(animalSemConjuntoDTO.getRaca().toUpperCase());
            entidade.setMaturidadeEnum(animalSemConjuntoDTO.getMaturidade());
            entidade.setEspecieEnum(animalSemConjuntoDTO.getEspecie());
            entidade.setPorteEnum(animalSemConjuntoDTO.getPorte());
            entidade.setSexoEnum(animalSemConjuntoDTO.getSexo());
            entidade.setOrigemEnum(animalSemConjuntoDTO.getOrigem());
            entidade.setComportamento(animalSemConjuntoDTO.getComportamento());
            entidade.setDoencas(animalSemConjuntoDTO.getDoencas());

            // Upload da imagem para o MinIO (se enviada)
            if (animalSemConjuntoDTO.getImagemPrincipalPerfil() != null && !animalSemConjuntoDTO.getImagemPrincipalPerfil().isEmpty()) {
                var arquivo = animalSemConjuntoDTO.getImagemPrincipalPerfil();
                String original = arquivo.getOriginalFilename() != null ? arquivo.getOriginalFilename() : "imagem.jpg";
                String sanitized = original.replaceAll("[^a-zA-Z0-9._-]", "_");
                String objectName = "animals/" + id + "/" + UUID.randomUUID() + "_" + sanitized;
                try {
                    minioService.upload(animalBucketName, objectName, arquivo.getInputStream(), arquivo.getSize(), arquivo.getContentType());
                    String url = minioService.getFileUrl(animalBucketName, objectName);
                    entidade.setImagemPrincipalPerfil(url != null ? url : objectName);
                } catch (IOException e) {
                    logger.error("Falha ao ler o arquivo para upload: {}", e.getMessage());
                    throw new PetXException("Não foi possível processar a imagem enviada.");
                }
            }

            entidade = animalRepository.save(entidade);
            return converterParaRespostaAnimalComConjuntoDTO(entidade);

        } catch (EntityNotFoundException e) {
            throw PetXException.animalNaoEncontrado(id.toString());
        } catch (DataIntegrityViolationException e) {
            throw new PetXException("Erro ao salvar: dados conflitantes ou inválidos.");
        } catch (JpaSystemException e) {
            throw new PetXException("Erro de conexão com o banco de dados.");
        }
    }

    @Transactional
    @CacheEvict(cacheNames = "stats", key = "'TOTAL_ANIMAIS'")
    public void deletarPorId(Long id) {
        try {
            logger.info("Iniciando a exclusão do animal com id: {}", id);

            var animal = animalRepository.findById(id)
                    .orElseThrow(() -> PetXException.animalNaoEncontrado("Animal com id: " + id + " não encontrado"));

            logger.info("Animal encontrado, iniciando processo de exclusão de relacionamentos");

            logger.info("Removendo relacionamentos com tutores");
            tutorRepository.removeAnimalFromTutor(animal);

            logger.info("Removendo relacionamentos com doenças");
            animal.setDoencas(new HashSet<>());

            logger.info("Removendo relacionamento com ONG");
            if (animal.getOng() != null) {
                animal.getOng().getAnimais().remove(animal);
                animal.setOng(null);
            }

            logger.info("Removendo relacionamentos de conjunto de animais");
            animalConjuntoRepository.deleteAnimalConjuntoByAnimalConjuntoId(animal.getId());

            logger.info("Limpando referências do animal");
            animal.setTutores(new HashSet<>());

            var entidadeArquivada = animal.getId();
            var chipIdArquivado = animal.getChipId() != null ? animal.getChipId() : "N/A";

            logger.info("Excluindo o animal");
            animalRepository.delete(animal);
            animalRepository.flush();

            logger.info("Iniciando o salvamento do log de atividade");
            this.logAtividadeService.log(
                    "Animal",
                    "DELETE",
                    animal.getId(),
                    "Animal excluído: " + animal.getNome() + " (ID: " + animal.getId() + ")",
                    SecurityUtils.requireUsername(),
                    Map.of("animalId", entidadeArquivada, "chipId", chipIdArquivado)
            );

            logger.info("Animal excluído com sucesso");

        } catch (Exception e) {
            logger.error("Erro ao excluir animal: ", e);
            throw PetXException.recursoNaoEncontrado("Erro ao tentar excluir o animal: ", id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AnimalGenericoResposta buscarAnimalPorId(Long id) {
        return animalUtils.buscarAnimalPorIdComConjuntoResposta(id);
    }

    @Override
    @Transactional
    public void declararObito(AnimalObituarioResquisicao obiturario) {
        animalRepository.findAnimalByChipId(obiturario.chipId())
                .ifPresentOrElse(animal -> {

                    if (animal.equals(true)) {
                        throw PetXException.animalJaFalecido("Animal com CHIP: " + obiturario.chipId());
                    }

                    var lista = animal.getTutores();
                    if (lista == null || lista.isEmpty()) {
                        obitoRepository.save(Obito.builder()
                                .dataObito(obiturario.dataObito())
                                .motivoObito(obiturario.motivoObito())
                                .animal(animal)
                                .build());
                    } else {
                        lista.forEach(tutor -> {
                            tutor.getAnimais().remove(animal);
                        });
                        animalRepository.save(animal);
                    }
                }, () -> {
                    throw PetXException.animalNaoEncontrado(obiturario.chipId());
                });
    }

    @Override
    @Transactional(readOnly = true)
    public AnimalGenericoResposta buscarAnimalPorChip(String chip) {

        Animal existeAnimal = animalRepository.findAnimalByChipId(chip).orElseThrow(() -> PetXException.animalNaoEncontrado(chip));

        return converterParaRespostaAnimalComConjuntoDTO(existeAnimal);
    }

    @Transactional(readOnly = true)
    public Page<AnimalPaginadoResposta> paginarAnimais(
            String nome, String raca,
            EspecieEnum especie, PorteEnum porte,
            SaudeEnum saude, String comportamento,
            MaturidadeEnum maturidade, OrigemAnimalEnum origem,
            SexoEnum sexo, AdocaoEnum adotado,
            Pageable pageable
    ) {
        var pageResult = animalRepository.findAll(
                AnimalSpecs.filtro(nome, raca, especie, porte, saude, comportamento, maturidade, origem, sexo, adotado),
                pageable
        );

       this.getImagemPerfilAnimal(pageResult);

        var content = AnimalMapper.converteAnimaisParaAnimalPaginadoResposta(
                pageResult.getContent()
        );

        return new PageImpl<>(content, pageable, pageResult.getTotalElements());
}

    @Override
    public Long contarQuantidadeAnimais() {

        System.out.println("qunatiadeeeeeeeeeeee" + animalRepository.count());

        return animalRepository.count();
    }

    private void verificarSeOAnimalNaoEstaFalecido(Animal animal) {
        if (obitoRepository.findByAnimalId(animal.getId()) != null) {
            throw PetXException.animalJaFalecido(animal.getChipId());
        }
    }

    @Override
    @Transactional
    public void atualizarPerfilBasico(Long id, String nome, String raca, String especie, String porte,
                                      String sexo, String maturidade, String origem, String corPelagem) {
        Animal entidade = animalRepository.findById(id)
                .orElseThrow(() -> PetXException.animalNaoEncontrado(id.toString()));
        if (nome != null) entidade.setNome(nome);
        if (raca != null) entidade.setRaca(raca.toUpperCase());
        if (especie != null) {
            try { entidade.setEspecieEnum(EspecieEnum.valueOf(especie)); } catch (IllegalArgumentException ignored) {}
        }
        if (porte != null) {
            try { entidade.setPorteEnum(PorteEnum.valueOf(porte)); } catch (IllegalArgumentException ignored) {}
        }
        if (sexo != null) {
            try { entidade.setSexoEnum(SexoEnum.valueOf(sexo)); } catch (IllegalArgumentException ignored) {}
        }
        if (maturidade != null) {
            try { entidade.setMaturidadeEnum(MaturidadeEnum.valueOf(maturidade)); } catch (IllegalArgumentException ignored) {}
        }
        if (origem != null) {
            try { entidade.setOrigemEnum(OrigemAnimalEnum.valueOf(origem)); } catch (IllegalArgumentException ignored) {}
        }
        if (corPelagem != null) entidade.setCorPelagem(corPelagem);
        animalRepository.save(entidade);
    }

    @Override
    @Transactional
    public void atualizarResumoSaude(Long id, String doencasLista) {
        Animal entidade = animalRepository.findById(id)
                .orElseThrow(() -> PetXException.animalNaoEncontrado(id.toString()));
        if (doencasLista != null) {
            // assumindo que Animal#doencas é Set<String> ou similar - adaptar se necessário
            Set<String> novas = new HashSet<>();
            for (String p : doencasLista.split(",")) {
                String t = p.trim();
                if (!t.isEmpty()) novas.add(t);
            }
            entidade.setDoencas(novas);
        }
        animalRepository.save(entidade);
    }

    @Override
    @CacheEvict(cacheNames = "stats", key = "'TOTAL_ANIMAIS'")
    public AnimalGenericoResposta salvarAnimal(AnimalGenericoRequisicao animalGenericoRequisicao) {

        var animal = AnimalMapper.converterParaAnimal(animalGenericoRequisicao);
        var ong = ongRepository.findById(1L);
        if (ong.isPresent()) {
            animal.setOng(ong.get());
            animal = animalRepository.save(animal);
            return AnimalMapper.converterParaRespostaAnimalComConjuntoDTO(animal);
        }
        return null;
    }

    @Transactional(readOnly = true)
    public Page<AnimalPaginadoResposta> paginarAnimaisParaAdocao(
            String nome,
            String raca,
            EspecieEnum especie,
            PorteEnum porte,
            SaudeEnum saude,
            String comportamento,
            MaturidadeEnum maturidade,
            OrigemAnimalEnum origem,
            SexoEnum sexo,
            Pageable pageable) {


            Page<Animal> pageContent = animalRepository.findAll(
                    AnimalSpecs.filtro(nome, raca, especie, porte, saude, comportamento, maturidade, origem, sexo, AdocaoEnum.DISPONIVEL),
                    pageable
            );

            this.getImagemPerfilAnimal(pageContent);
            var converteListaAnimal = AnimalMapper.converteAnimaisParaAnimalPaginadoResposta(pageContent.getContent());

        return new PageImpl<>(converteListaAnimal, pageable, pageContent.getTotalElements());
    }

    @Override
    public long getTotalAdocoes() {
        return animalRepository.countByAdotado(AdocaoEnum.ADOTADO);
    }

    private void getImagemPerfilAnimal(Page<Animal> pageContent) {
         pageContent.getContent().forEach(animal -> {
            String imgUrl = mediaService.getProfilePresignedUrl(MediaTargetType.ANIMAL, animal.getId());
            animal.setImagemPrincipalPerfil(imgUrl != null ? imgUrl : "");
        });

    }

}

