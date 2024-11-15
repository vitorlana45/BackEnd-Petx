package org.ong.pet.pex.backendpetx.service;

import org.ong.pet.pex.backendpetx.dto.request.CadastrarTutorRequisicao;
import org.ong.pet.pex.backendpetx.dto.response.TutorDTOResponse;

import java.util.Set;

public interface TutorService {

    Long cadastrarTutor(CadastrarTutorRequisicao cadastrarTutorRequisicao);
    void deletarTutor(String cpf);
    Set<TutorDTOResponse> buscarTodosTutores();
    TutorDTOResponse buscarTutorPorCpf(String cpf);
    void atualizarDadosTutor(String cpf, CadastrarTutorRequisicao cadastrarTutorRequisicao);
    void deletarTutorPorCpf(String cpf);

}
