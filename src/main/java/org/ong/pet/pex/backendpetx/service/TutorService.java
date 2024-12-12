package org.ong.pet.pex.backendpetx.service;

import org.ong.pet.pex.backendpetx.dto.request.AtualizarTutorRequisicao;
import org.ong.pet.pex.backendpetx.dto.request.CadastrarTutorRequisicao;
import org.ong.pet.pex.backendpetx.dto.response.TutorDTOResposta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TutorService {

    Long cadastrarTutor(CadastrarTutorRequisicao cadastrarTutorRequisicao);

    TutorDTOResposta buscarTutorPorCpf(String cpf);

    String atualizarDadosTutor(String cpfAntigo, AtualizarTutorRequisicao cadastrarTutorRequisicao);

    void deletarTutorPorCpf(String cpf);

    void deletarTutorPorId(Long id);

    Page<TutorDTOResposta> findAllTutorPaginacao(String nome, String cep, String cidade,String estado, Integer idade, Pageable pageable);

}
