package org.ong.pet.pex.backendpetx.service.impl;

import org.ong.pet.pex.backendpetx.dto.request.BoletimDTORequisicao;
import org.ong.pet.pex.backendpetx.dto.response.BoletimDTOResposta;
import org.ong.pet.pex.backendpetx.entities.Animal;
import org.ong.pet.pex.backendpetx.entities.Boletim;
import org.ong.pet.pex.backendpetx.enums.Destino;
import org.ong.pet.pex.backendpetx.enums.SexoEnum;
import org.ong.pet.pex.backendpetx.repositories.AnimalRepository;
import org.ong.pet.pex.backendpetx.repositories.BoletimRepository;
import org.ong.pet.pex.backendpetx.repositories.OngRepository;
import org.ong.pet.pex.backendpetx.service.BoletimService;
import org.ong.pet.pex.backendpetx.service.exceptions.PetXException;
import org.ong.pet.pex.backendpetx.service.mappers.AnimalMapper;
import org.ong.pet.pex.backendpetx.service.mappers.BoletimMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
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
    public BoletimDTOResposta createBoletim(BoletimDTORequisicao boletimDTO) {


        if( boletimDTO.getAnimal().isAnimalEMaezinha() && !boletimDTO.getAnimal().getSexo().equals(SexoEnum.FEMEA))
            throw new PetXException("Animal não pode ser maezinha se não for fêmea");

        if(!boletimDTO.getAnimal().isAnimalEMaezinha() && boletimDTO.getAnimal().getMaezinhaComFilhotes() != null)
            throw new PetXException("Animal não pode ter filhotes se não for maezinha");

        System.out.println("BoletimDTO: " + boletimDTO);

        Boletim boletim = boletimMapper.converteParaEntidade(boletimDTO);

        if (boletimDTO.getAnimal() == null)
            throw new PetXException("Animal não pode ser nulo");

        Animal newAnimal = AnimalMapper.converterParaAnimal(boletimDTO.getAnimal());
        newAnimal.setOng(ongRepository.findById(1L).orElse(null));
        newAnimal = animalRepository.save(newAnimal);
        
        boletim.setAnimal(newAnimal);
        boletim.setOng(newAnimal.getOng());
        
        // Save Boletim entity
        boletim = boletimRepository.save(boletim);
        
        // Set the bidirectional relationship
        newAnimal.setBoletim(boletim);
        animalRepository.save(newAnimal);
        
        return boletimMapper.converteParaDTO(boletim);
    }

    @Override
    @Transactional(readOnly = true)
    public BoletimDTOResposta getBoletim(Long id) {
        Boletim boletim = boletimRepository.findById(id)
                .orElseThrow(() -> new PetXException("Boletim não encontrado"));
        return boletimMapper.converteParaDTO(boletim);
    }

    @Transactional
    @Override
    public void deleteBoletim(Long id) {
        Boletim boletim = boletimRepository.findById(id)
                .orElseThrow(() -> new PetXException("Boletim não encontrado"));

        if (boletim.getAnimal() != null) {
            boletim.getAnimal().setOng(null);
            boletim.getAnimal().setBoletim(null);
            boletim.setAnimal(null);
        }
        boletim.setOng(null);
        boletimRepository.delete(boletim);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BoletimDTOResposta> findAllBoletins(
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
    public BoletimDTOResposta updateBoletim(Long id, BoletimDTORequisicao dto) {
        Boletim boletim = boletimRepository.findById(id)
                .orElseThrow(() -> new PetXException("Boletim não encontrado"));

        aplicarCamposPresentes(dto, boletim);

        if(dto.getAnimal() != null){
            Animal newAnimal = AnimalMapper.converterParaAnimal(dto.getAnimal());
            newAnimal.setOng(ongRepository.findById(1L).orElse(null));
            newAnimal = animalRepository.save(newAnimal);
            boletim.setAnimal(newAnimal);
            
            // Set the bidirectional relationship
            newAnimal.setBoletim(boletim);
            animalRepository.save(newAnimal);
        }

        boletim = boletimRepository.save(boletim);
        return boletimMapper.converteParaDTO(boletim);
    }

    private void aplicarCamposPresentes(BoletimDTORequisicao dto, Boletim boletim) {
        Optional.ofNullable(dto.getNumeroOcorrencia()).ifPresent(boletim::setNumeroOcorrencia);
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