package org.ong.pet.pex.backendpetx.service;

import org.ong.pet.pex.backendpetx.dto.request.CadastrarTutorRequisicao;

public interface TutorService {

    void cadastrarTutor(CadastrarTutorRequisicao cadastrarTutorRequisicao);
    void deletarTutor(String cpf);
    void buscarTodosTutores();
    void buscarTutorPorCpf(String cpf);
    void atualizarDadosTutor(String cpf, CadastrarTutorRequisicao cadastrarTutorRequisicao);
    void deletarTutorPorCpf(String cpf);

}
