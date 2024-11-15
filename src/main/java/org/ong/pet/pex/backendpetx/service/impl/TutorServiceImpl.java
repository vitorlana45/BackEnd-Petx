package org.ong.pet.pex.backendpetx.service.impl;

import org.ong.pet.pex.backendpetx.dto.request.CadastrarTutorRequisicao;
import org.ong.pet.pex.backendpetx.repositories.AnimalRepository;
import org.ong.pet.pex.backendpetx.repositories.OngRepository;
import org.ong.pet.pex.backendpetx.repositories.TutorRepository;
import org.ong.pet.pex.backendpetx.service.TutorService;
import org.springframework.stereotype.Service;

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


    @Override
    public void cadastrarTutor(CadastrarTutorRequisicao cadastrarTutorRequisicao) {

        var a = tutorRepository.findTutorByCpf(cadastrarTutorRequisicao.cpf()).orElse(null);




    }

    @Override
    public void deletarTutor(String cpf) {

    }

    @Override
    public void buscarTodosTutores() {

    }

    @Override
    public void buscarTutorPorCpf(String cpf) {

    }

    @Override
    public void atualizarDadosTutor(String cpf, CadastrarTutorRequisicao cadastrarTutorRequisicao) {

    }

    @Override
    public void deletarTutorPorCpf(String cpf) {

    }
}
