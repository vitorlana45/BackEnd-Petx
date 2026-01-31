package org.ong.pet.pex.backendpetx.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final AnimalService animalService;
    private final TutorService tutorService;
    private final BoletimService boletimService;


    @Override
    @Cacheable(cacheNames = "stats", key = "'TOTAL_ANIMAIS'")
    public long getQuantidadeAnimais() {
        return animalService.contarQuantidadeAnimais();
    }

    @Override
    public long getQuantidadeAdocoes() {
        return animalService.getTotalAdocoes();
    }

    @Override
    @Cacheable(cacheNames = "stats", key = "'TOTAL_BOLETINS'")
    public long getTotalBoletins() {
        return boletimService.contarTotalBoletins();
    }

    @Override
    @Cacheable(cacheNames = "stats", key = "'TOTAL_TUTORES'")
    public long getQuantidadeTutores() {
        return tutorService.getTotalTutores();
    }
}
