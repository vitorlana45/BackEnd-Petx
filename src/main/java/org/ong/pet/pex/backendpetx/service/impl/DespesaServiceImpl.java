package org.ong.pet.pex.backendpetx.service.impl;

import org.ong.pet.pex.backendpetx.dto.DespesaRequisicaoDinamica;
import org.ong.pet.pex.backendpetx.dto.request.DespesaRequisicao;
import org.ong.pet.pex.backendpetx.dto.response.DespesaResposta;
import org.ong.pet.pex.backendpetx.dto.response.ListarDespesaResposta;
import org.ong.pet.pex.backendpetx.entity.Despesa;
import org.ong.pet.pex.backendpetx.enums.CategoriaDespesaEnum;
import org.ong.pet.pex.backendpetx.enums.FormaPagamentoEnum;
import org.ong.pet.pex.backendpetx.enums.StatusDespesaEnum;
import org.ong.pet.pex.backendpetx.repository.DespesaRepository;
import org.ong.pet.pex.backendpetx.service.DespesaService;
import org.ong.pet.pex.backendpetx.service.exceptions.DespesaException;
import org.ong.pet.pex.backendpetx.service.mappers.DespesaMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Service
public class DespesaServiceImpl implements DespesaService {

    private final DespesaRepository despesaRepository;
    private final DespesaMapper despesaMapper;

    public DespesaServiceImpl(DespesaRepository despesaRepository, DespesaMapper despesaMapper) {

        this.despesaRepository = despesaRepository;
        this.despesaMapper = despesaMapper;
    }

    @Override
    @Transactional
    public DespesaResposta cadastrarDespesa(final DespesaRequisicao dto) {
        Despesa despesa = despesaRepository.save(despesaMapper.mapearParaEntidade(dto));
        return despesaMapper.mapearParaDTO(despesa);
    }

    @Override
    @Transactional
    public void deletarDespesa(final Long id) {
        var entidade = despesaRepository.findById(id).orElseThrow(DespesaException::despesaNaoEncontrada);
        despesaRepository.deleteById(entidade.getId());
    }

    @Override
    @Transactional
    public DespesaResposta atualizarDespesa(final Long id, final DespesaRequisicao dto) {
        Despesa despesaBanco = despesaRepository.findById(id)
                .orElseThrow(() -> new DespesaException("Despesa nao encontrada", HttpStatus.NOT_FOUND));

        // Atualiza os dados da entidade existente
        despesaBanco.setDescricao(dto.descricao());
        despesaBanco.setValor(dto.valor());
        despesaBanco.setCategoria(dto.categoria());
        despesaBanco.setFormaPagamento(dto.formaPagamento());
        despesaBanco.setDataPrevistaPagamento(dto.dataPrevistaPagamento());
        despesaBanco.setDataPagamento(dto.dataPagamento());
        despesaBanco.setStatusDespesa(dto.statusDespesa());

        Despesa despesaSalva = despesaRepository.save(despesaBanco);
        return despesaMapper.mapearParaDTO(despesaSalva);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ListarDespesaResposta> paginarDespesa(String descricao,
                                                      CategoriaDespesaEnum categoria,
                                                      StatusDespesaEnum status,
                                                      FormaPagamentoEnum formaPagamento,
                                                      LocalDate dataPagamento,
                                                      LocalDate dataPrevistaPagamento,
                                                      BigDecimal valor,
                                                      Pageable pageable) {

        Page<Despesa> despesas = despesaRepository.findAllDespesa(
                descricao,
                valor,
                dataPrevistaPagamento,
                categoria != null ? categoria.name() : null,
                formaPagamento != null ? formaPagamento.name() : null,
                dataPagamento,
                status != null ? status.name() : null,
                pageable
        );

        List<ListarDespesaResposta> dtos = despesaMapper.mapearListaParaDTO(despesas.getContent());
        return new PageImpl<>(dtos, pageable, despesas.getTotalElements());
    }


    @Transactional
    public DespesaResposta atualizarDespesaDinamicamente(final Long id, final DespesaRequisicaoDinamica dto) {
        var entidade = despesaRepository.findById(id).orElseThrow(DespesaException::despesaNaoEncontrada);

//        if (dto.descricao() != null) entidade.setDescricao(dto.descricao());
//        if (dto.valor() != null) entidade.setValor(dto.valor());
//        if (dto.categoria() != null) entidade.setCategoria(dto.categoria());
//        if (dto.formaPagamento() != null) entidade.setFormaPagamento(dto.formaPagamento());
//        if (dto.dataPrevistaPagamento() != null) entidade.setDataPrevistaPagamento(dto.dataPrevistaPagamento());
//        if (dto.dataPagamento() != null) entidade.setDataPagamento(dto.dataPagamento());
//        if (dto.statusDespesa() != null) entidade.setStatusDespesa(dto.statusDespesa());

        return despesaMapper.mapearParaDTO(entidade);
    }

    @Override
    public Despesa buscarDespesaPorId(Long id) {
        return despesaRepository.findById(id).orElseThrow(DespesaException::despesaNaoEncontrada);
    }

}