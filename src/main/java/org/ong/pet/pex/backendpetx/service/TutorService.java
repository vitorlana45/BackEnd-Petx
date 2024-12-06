package org.ong.pet.pex.backendpetx.service;

import org.ong.pet.pex.backendpetx.dto.request.AtualizarTutorRequisicao;
import org.ong.pet.pex.backendpetx.dto.request.CadastrarTutorRequisicao;
import org.ong.pet.pex.backendpetx.dto.response.TutorDTOResponse;

import java.util.Set;

public interface TutorService {

    Long cadastrarTutor(CadastrarTutorRequisicao cadastrarTutorRequisicao);

    Set<TutorDTOResponse> buscarTodosTutores();

    TutorDTOResponse buscarTutorPorCpf(String cpf);

    String atualizarDadosTutor(String cpfAntigo, AtualizarTutorRequisicao cadastrarTutorRequisicao);

    void deletarTutorPorCpf(String cpf);

    void deletarTutorPorId(Long id);
}
