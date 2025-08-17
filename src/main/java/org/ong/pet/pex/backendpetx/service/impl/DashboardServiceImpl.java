package org.ong.pet.pex.backendpetx.service.impl;

import org.ong.pet.pex.backendpetx.service.AnimalService;
import org.ong.pet.pex.backendpetx.service.ConsumoAlimentoService;
import org.ong.pet.pex.backendpetx.service.DashboardService;
import org.ong.pet.pex.backendpetx.service.TutorService;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final TutorService tutorService;
    private final AnimalService animalService;
    private final ConsumoAlimentoService consumoAlimentoService;

    public DashboardServiceImpl(TutorService tutorService, AnimalService animalService, ConsumoAlimentoService consumoAlimentoService) {
        this.tutorService = tutorService;
        this.animalService = animalService;
        this.consumoAlimentoService = consumoAlimentoService;
    }


    @Override
    public Long getTotalAnimais() {
        return animalService.contarQuantidadeAnimais();
    }

    @Override
    public Long totalConsumo() {
        return 220L;
    }


    @Override
    public Long getTotalTutores() {
        return tutorService.getTotalTutores();
    }
}
