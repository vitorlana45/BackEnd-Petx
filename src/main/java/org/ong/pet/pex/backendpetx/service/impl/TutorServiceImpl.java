package org.ong.pet.pex.backendpetx.service.impl;

import org.ong.pet.pex.backendpetx.entities.Tutor;

import org.ong.pet.pex.backendpetx.repositories.TutorRepository;
import org.ong.pet.pex.backendpetx.service.TutorService;
import org.ong.pet.pex.backendpetx.service.exceptions.UsuarioNaoEncontrado;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TutorServiceImpl implements TutorService {

    private final TutorRepository tutorRepository;
     private final  PasswordEncoder passwordEncoder;

    public TutorServiceImpl(TutorRepository tutorRepository, PasswordEncoder passwordEncoder) {
        this.tutorRepository = tutorRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void delete(Long id) {

    }

    @Override
    public Tutor save(Tutor tutor) {
        return null;
    }

    @Override
    public List<Tutor> getTutors() {
        return tutorRepository.findAll();
    }

    @Override
    public Tutor getTutor(String email) {


        if (tutorRepository.findByEmail(email) == null) {
           throw new UsuarioNaoEncontrado("Usuário não encontrado");
        }
            return tutorRepository.findByEmail(email);
    }
    public Tutor update(String email, Tutor newTutor) {
//        if (tutorRepository.findByEmail(email) == null){
////            String passwordHash = passwordEncoder.encode(newTutor);
////            Tutor entity = new Tutor(newTutor.getIdade(), newTutor.getTelefone(),
////                  newTutor.getAnimal(),newTutor.getContato());
//        }
        return null;
    }

}
