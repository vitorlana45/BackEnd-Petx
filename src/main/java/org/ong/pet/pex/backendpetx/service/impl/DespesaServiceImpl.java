package org.ong.pet.pex.backendpetx.service.impl;

import org.ong.pet.pex.backendpetx.dto.request.DespesaRequisicaoDTO;
import org.ong.pet.pex.backendpetx.dto.request.DespesaRequisicaoDinamicaDTO;
import org.ong.pet.pex.backendpetx.dto.response.DespesaDTORespota;
import org.ong.pet.pex.backendpetx.dto.response.ListarDespesaResposta;
import org.ong.pet.pex.backendpetx.entities.Despesa;
import org.ong.pet.pex.backendpetx.enums.CategoriaDespesaEnum;
import org.ong.pet.pex.backendpetx.enums.FormaPagamentoEnum;
import org.ong.pet.pex.backendpetx.enums.StatusDespesaEnum;
import org.ong.pet.pex.backendpetx.repositories.DespesaRepository;
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
    public DespesaDTORespota cadastrarDespesa(final DespesaRequisicaoDTO dto) {
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
    public DespesaDTORespota atualizarDespesa(final Long id, final DespesaRequisicaoDTO dto) {
        if (despesaRepository.existsById(id)) {
            Despesa despesa = despesaRepository.save(despesaMapper.mapearParaEntidade(dto));
            return despesaMapper.mapearParaDTO(despesa);
        }
        throw new DespesaException("Despesa nao encontrado", HttpStatus.NOT_FOUND);
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

    @Override
    @Transactional
    public DespesaDTORespota atualizarDespesaDinamicamente(final Long id, final DespesaRequisicaoDinamicaDTO dto) {
        var entidade = despesaRepository.findById(id).orElseThrow(DespesaException::despesaNaoEncontrada);

        if (dto.descricao() != null) entidade.setDescricao(dto.descricao());
        if (dto.valor() != null) entidade.setValor(dto.valor());
        if (dto.categoria() != null) entidade.setCategoria(dto.categoria());
        if (dto.formaPagamento() != null) entidade.setFormaPagamento(dto.formaPagamento());
        if (dto.dataPrevistaPagamento() != null) entidade.setDataPrevistaPagamento(dto.dataPrevistaPagamento());
        if (dto.dataPagamento() != null) entidade.setDataPagamento(dto.dataPagamento());
        if (dto.statusDespesa() != null) entidade.setStatusDespesa(dto.statusDespesa());

        return despesaMapper.mapearParaDTO(entidade);
    }
}