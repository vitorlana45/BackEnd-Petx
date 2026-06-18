package org.ong.pet.pex.backendpetx.service.impl;

import org.ong.pet.pex.backendpetx.dto.request.BoletimRequisicao;
import org.ong.pet.pex.backendpetx.dto.request.AnimalGenericoRequisicao;
import org.ong.pet.pex.backendpetx.dto.response.BoletimResposta;
import org.ong.pet.pex.backendpetx.entity.Animal;
import org.ong.pet.pex.backendpetx.entity.Boletim;
import org.ong.pet.pex.backendpetx.enums.AdocaoEnum;
import org.ong.pet.pex.backendpetx.enums.Destino;
import org.ong.pet.pex.backendpetx.enums.OrigemAnimalEnum;
import org.ong.pet.pex.backendpetx.enums.SexoEnum;
import org.ong.pet.pex.backendpetx.repository.AnimalRepository;
import org.ong.pet.pex.backendpetx.repository.BoletimRepository;
import org.ong.pet.pex.backendpetx.repository.OngRepository;
import org.ong.pet.pex.backendpetx.service.BoletimService;
import org.ong.pet.pex.backendpetx.service.exceptions.PetXException;
import org.ong.pet.pex.backendpetx.service.mappers.AnimalMapper;
import org.ong.pet.pex.backendpetx.service.mappers.BoletimMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Transactional
public class BoletimServiceImpl implements BoletimService {

    private final BoletimRepository boletimRepository;

    private final AnimalRepository animalRepository;

    private final BoletimMapper boletimMapper;

    private final OngRepository ongRepository;

    public BoletimServiceImpl(BoletimRepository boletimRepository, AnimalRepository animalRepository, BoletimMapper boletimMapper, OngRepository ongRepository) {
        this.boletimRepository = boletimRepository;
        this.animalRepository = animalRepository;
        this.boletimMapper = boletimMapper;
        this.ongRepository = ongRepository;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"stats"}, key = "'TOTAL_ANIMAIS'", allEntries = false)
    public BoletimResposta createBoletim(BoletimRequisicao boletimDTO) {

        if (boletimDTO == null || boletimDTO.getAnimal() == null) {
            throw new PetXException("Animal não pode ser nulo");
        }

        if( boletimDTO.getAnimal().isAnimalEMaezinha() && !boletimDTO.getAnimal().getSexo().equals(SexoEnum.FEMEA))
            throw new PetXException("Animal não pode ser maezinha se não for fêmea");

        if(!boletimDTO.getAnimal().isAnimalEMaezinha() && boletimDTO.getAnimal().getMaezinhaComFilhotes() != null)
            throw new PetXException("Animal não pode ter filhotes se não for maezinha");

        System.out.println("BoletimDTO: " + boletimDTO);

        Boletim boletim = boletimMapper.converteParaEntidade(boletimDTO);
        // número é sempre padronizado/gerado pelo sistema
        boletim.setNumeroOcorrencia(null);

        Animal newAnimal = AnimalMapper.converterParaAnimal(boletimDTO.getAnimal());
        newAnimal.setOng(ongRepository.findById(1L).orElse(null));
        newAnimal.setAdotado(AdocaoEnum.DISPONIVEL);

        boletim.setOng(newAnimal.getOng());
        boletim.addAnimal(newAnimal);

        boletim = salvarBoletimComNumeroGerado(boletim);
        
        return boletimMapper.converteParaDTO(boletim);
    }

    @Override
    @Transactional
    public BoletimResposta adicionarAnimalEmOcorrencia(Long numeroOcorrencia, AnimalGenericoRequisicao animalDto) {
        if (numeroOcorrencia == null) throw new PetXException("Número da ocorrência é obrigatório");
        if (animalDto == null) throw new PetXException("Animal não pode ser nulo");

        Boletim boletim = boletimRepository.findByNumeroOcorrencia(numeroOcorrencia)
                .orElseThrow(() -> new PetXException("Boletim não encontrado para o número informado"));

        Animal novo = AnimalMapper.converterParaAnimal(animalDto);
        novo.setAdotado(AdocaoEnum.DISPONIVEL);
        // herda a ONG do boletim quando existir
        novo.setOng(boletim.getOng() != null ? boletim.getOng() : ongRepository.findById(1L).orElse(null));

        boletim.addAnimal(novo);
        boletimRepository.save(boletim);
        return boletimMapper.converteParaDTO(boletim);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsNumeroOcorrenciaNaOng(Long numeroOcorrencia, Long ongId) {
        if (numeroOcorrencia == null || ongId == null) return false;
        return boletimRepository.existsByNumeroOcorrenciaAndOng_Id(numeroOcorrencia, ongId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoletimResposta> buscarOcorrenciasParaVinculo(Long ongId,
                                                                LocalDateTime inicio,
                                                                LocalDateTime fim,
                                                                OrigemAnimalEnum origem,
                                                                Destino destino,
                                                                int limit) {
        if (ongId == null) return List.of();
        int safeLimit = Math.max(1, Math.min(limit, 100));
        var pageable = org.springframework.data.domain.PageRequest.of(0, safeLimit,
                org.springframework.data.domain.Sort.by("dataAtendimento").descending());

        org.springframework.data.jpa.domain.Specification<Boletim> spec =
                (root, query, cb) -> {
                    var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();
                    predicates.add(cb.equal(root.get("ong").get("id"), ongId));
                    if (inicio != null) {
                        predicates.add(cb.greaterThanOrEqualTo(root.get("dataAtendimento"), inicio));
                    }
                    if (fim != null) {
                        predicates.add(cb.lessThanOrEqualTo(root.get("dataAtendimento"), fim));
                    }
                    if (origem != null) {
                        predicates.add(cb.equal(root.get("origem"), origem));
                    }
                    if (destino != null) {
                        predicates.add(cb.equal(root.get("destino"), destino));
                    }
                    return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
                };

        var page = boletimRepository.findAll(spec, pageable);
        return page.map(boletimMapper::converteParaDTO).getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public BoletimResposta getBoletim(Long id) {
        Boletim boletim = boletimRepository.findById(id)
                .orElseThrow(() -> new PetXException("Boletim não encontrado"));
        return boletimMapper.converteParaDTO(boletim);
    }

    @Transactional
    @Override
    public void deleteBoletim(Long id) {
        Boletim boletim = boletimRepository.findById(id)
                .orElseThrow(() -> new PetXException("Boletim não encontrado"));

        // desvincula/limpa todos os animais da ocorrência
        if (boletim.getAnimais() != null) {
            for (Animal a : new HashSet<>(boletim.getAnimais())) {
                a.setOng(null);
                a.setBoletim(null);
                boletim.removeAnimal(a);
            }
        }
        boletim.setOng(null);
        boletimRepository.delete(boletim);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BoletimResposta> findAllBoletins(
                                                    Long numeroOcorrencia,
//                                                    LocalDateTime dataInicio,
//                                                    LocalDateTime dataFim,
                                                    Destino destino,
                                                    Pageable pageable) {
//        String destinoStr = destino != null ? destino : "";
    Page<Boletim> boletins = boletimRepository.findAllBoletins(numeroOcorrencia, destino, pageable);
        return boletins.map(boletimMapper::converteParaDTO);
    }

    @Override
    @Transactional
    public BoletimResposta updateBoletim(Long id, BoletimRequisicao dto) {
        Boletim boletim = boletimRepository.findById(id)
                .orElseThrow(() -> new PetXException("Boletim não encontrado"));

        aplicarCamposPresentes(dto, boletim);

        if(dto.getAnimal() != null){
            Animal newAnimal = AnimalMapper.converterParaAnimal(dto.getAnimal());
            newAnimal.setOng(boletim.getOng() != null ? boletim.getOng() : ongRepository.findById(1L).orElse(null));
            newAnimal.setAdotado(AdocaoEnum.DISPONIVEL);
            // aqui, o update passa a anexar mais um animal na mesma ocorrência
            boletim.addAnimal(newAnimal);
        }

        boletim = boletimRepository.save(boletim);
        return boletimMapper.converteParaDTO(boletim);
    }

    private void aplicarCamposPresentes(BoletimRequisicao dto, Boletim boletim) {
        // numeroOcorrencia é imutável e gerado automaticamente
        Optional.ofNullable(dto.getDataAtendimento()).ifPresent(boletim::setDataAtendimento);
        Optional.ofNullable(dto.getDestino()).ifPresent(boletim::setDestino);
        Optional.ofNullable(dto.getMotivoRecolhimento()).ifPresent(boletim::setMotivoRecolhimento);
        Optional.ofNullable(dto.getObservacaoClinica()).ifPresent(boletim::setObservacaoClinica);
        Optional.ofNullable(dto.getRuaAvenida()).ifPresent(boletim::setRuaAvenida);
        Optional.ofNullable(dto.getCidade()).ifPresent(boletim::setCidade);
        Optional.ofNullable(dto.getMunicipio()).ifPresent(boletim::setMunicipio);
        Optional.ofNullable(dto.getBairro()).ifPresent(boletim::setBairro);
        Optional.ofNullable(dto.getEstado()).ifPresent(boletim::setEstado);
        Optional.ofNullable(dto.getNomeDenuncianteOuTutor()).ifPresent(boletim::setNomeDenuncianteOuTutor);
        Optional.ofNullable(dto.getCpfDenuncianteOuTutor()).ifPresent(boletim::setCpfDenuncianteOuTutor);
        Optional.ofNullable(dto.getTelefoneDenuncianteOuTutor()).ifPresent(boletim::setTelefoneDenuncianteOuTutor);
    }

    private Boletim salvarBoletimComNumeroGerado(Boletim boletim) {
        // padrão: YYYY + 5 dígitos (ex: 202500123)
        LocalDateTime baseData = boletim.getDataAtendimento() != null ? boletim.getDataAtendimento() : LocalDateTime.now();
        int year = baseData.getYear();
        long inicio = year * 100_000L;
        long fim = inicio + 99_999L;

        for (int attempt = 0; attempt < 5; attempt++) {
            Long max = boletimRepository.findMaxNumeroOcorrenciaInRange(inicio, fim);
            long next = (max == null) ? (inicio + 1) : (max + 1);
            if (next > fim) {
                throw new PetXException("Limite anual de números de ocorrência atingido para " + year);
            }

            boletim.setNumeroOcorrencia(next);
            try {
                return boletimRepository.save(boletim);
            } catch (DataIntegrityViolationException e) {
                // colisão rara por concorrência; tenta novamente
                if (attempt == 4) {
                    throw new PetXException("Não foi possível gerar um número de ocorrência único no momento. Tente novamente.");
                }
            }
        }
        throw new PetXException("Não foi possível gerar número de ocorrência");
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> obterEstatisticasResgates(int mesesSérieTemporal) {
        Map<String, Object> resultado = new HashMap<>();

    // Totais agregados (origem agora direta da entidade Animal para evitar nulos artificiais)
    resultado.put("porOrigem", mapearListaSemNull(animalRepository.countByOrigemAnimal()));
        resultado.put("porDestino", mapearLista(boletimRepository.countByDestino()));
        resultado.put("porEspecie", mapearLista(boletimRepository.countByEspecie()));

        LocalDateTime inicio = LocalDateTime.now().minusMonths(mesesSérieTemporal).truncatedTo(ChronoUnit.DAYS).withDayOfMonth(1);
        List<Map<String,Object>> serieBruta = mapearSerie(boletimRepository.countByMesDesde(inicio));
        // Preenche meses faltantes com zero para melhorar visualização
        Map<String, Long> mapa = new LinkedHashMap<>();
        LocalDateTime cursor = inicio;
        LocalDateTime agora = LocalDateTime.now();
        while (!cursor.isAfter(agora)) {
            String key = cursor.getYear()+"-"+String.format("%02d", cursor.getMonthValue());
            mapa.put(key, 0L);
            cursor = cursor.plusMonths(1);
        }
        for (Map<String,Object> m : serieBruta) {
            String periodo = (String) m.get("periodo");
            Long total = ((Number) m.get("total")).longValue();
            mapa.put(periodo, total);
        }
        List<Map<String,Object>> serieCompleta = new ArrayList<>();
        for (var e : mapa.entrySet()) {
            Map<String,Object> m = new HashMap<>();
            m.put("periodo", e.getKey());
            m.put("total", e.getValue());
            serieCompleta.add(m);
        }
        resultado.put("serieTemporal", serieCompleta);
        resultado.put("inicioPeriodo", inicio);
        resultado.put("meses", mesesSérieTemporal);
        return resultado;
    }

    @Override
    public long contarTotalBoletins() {
        return boletimRepository.count();
    }

    private List<Map<String, Object>> mapearLista(List<Object[]> rows) {
        List<Map<String, Object>> lista = new ArrayList<>();
        for (Object[] row : rows) {
            Map<String, Object> map = new HashMap<>();
            Object labelObj = row[0];
            String label;
            if (labelObj == null) {
                label = "Sem Origem";
            } else if (labelObj instanceof Enum<?>) {
                // Exibe descrição se enum possuir método getDescricao / getStatus
                Enum<?> en = (Enum<?>) labelObj;
                try {
                    java.lang.reflect.Method m;
                    if ((m = en.getClass().getMethod("getDescricao")) != null) {
                        Object val = m.invoke(en);
                        label = val != null ? val.toString() : en.name();
                    } else {
                        label = en.name();
                    }
                } catch (NoSuchMethodException ns) {
                    try {
                        java.lang.reflect.Method m2 = en.getClass().getMethod("getStatus");
                        Object val2 = m2.invoke(en);
                        label = val2 != null ? val2.toString() : en.name();
                    } catch (Exception ex2) {
                        label = en.name();
                    }
                } catch (Exception e) {
                    label = en.name();
                }
                // Ajuste final: transforma CONSTANTES_EM_CAIXA em Capitalizado
                if (label.equals(label.toUpperCase())) {
                    label = label.toLowerCase().replace('_',' ');
                    label = java.util.Arrays.stream(label.split(" "))
                            .map(s -> s.isEmpty()? s : Character.toUpperCase(s.charAt(0))+s.substring(1))
                            .reduce((a,b)->a+" "+b).orElse(label);
                }
            } else {
                label = labelObj.toString();
            }
            map.put("label", label);
            map.put("total", ((Number) row[1]).longValue());
            lista.add(map);
        }
    // Se só existe um item "Sem Origem" mas total geral >0 e deveria haver enums, mantemos; caso queira ocultar zeros futuramente, aplicar aqui.
    return lista;
    }

    private List<Map<String,Object>> mapearListaSemNull(List<Object[]> rows){
        List<Map<String,Object>> base = mapearLista(rows);
        long countSemOrigem = base.stream().filter(m -> "Sem Origem".equals(m.get("label"))).map(m-> (Long)m.get("total")).reduce(0L,Long::sum);
        if(countSemOrigem > 0 && base.size() == 1){
            // apenas Sem Origem -> provavelmente dados reais nulos (manter)
            return base;
        }
        if(base.size() > 1){
            base.removeIf(m -> "Sem Origem".equals(m.get("label")));
        }
        return base;
    }

    private List<Map<String, Object>> mapearSerie(List<Object[]> rows) {
        List<Map<String, Object>> lista = new ArrayList<>();
        for (Object[] r : rows) {
            Map<String, Object> m = new HashMap<>();
            Object periodoRaw = r[0];
            String label;
            if (periodoRaw instanceof java.time.LocalDateTime ldt) {
                label = ldt.getYear() + "-" + String.format("%02d", ldt.getMonthValue());
            } else if (periodoRaw instanceof java.sql.Timestamp ts) {
                java.time.LocalDateTime ldt = ts.toLocalDateTime();
                label = ldt.getYear() + "-" + String.format("%02d", ldt.getMonthValue());
            } else {
                label = String.valueOf(periodoRaw);
            }
            m.put("periodo", label);
            m.put("total", ((Number) r[1]).longValue());
            lista.add(m);
        }
        return lista;
    }

}