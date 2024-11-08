package org.ong.pet.pex.backendpetx.service;

import org.ong.pet.pex.backendpetx.entities.Tutor;

import java.util.List;

public interface TutorService {
    List<Tutor> getTutors();
    Tutor getTutor(String email);
//    Tutor update(String email, Tutor tutor);
    void delete(Long id);
    Tutor save(Tutor tutor);

}
