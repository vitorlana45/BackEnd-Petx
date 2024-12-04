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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<ListarDespesaResposta> listarDespesa() {
        return despesaMapper.mapearListaParaDTO(despesaRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ListarDespesaResposta> listarDespesaFiltrada(final String filtro) {
        try {
            CategoriaDespesaEnum categoria = CategoriaDespesaEnum.valueOf(filtro.toUpperCase());
            var lista = despesaMapper.mapearListaParaDTO(despesaRepository.listarDespesaFiltrada(categoria));
            return lista.stream()
                    .filter(despesa -> despesa.categoria().equals(categoria)).toList();

        } catch (IllegalArgumentException ex) {
            throw DespesaException.filtroDespesaInvalido(filtro);
        }
    }

    @Override
    @Transactional
    public DespesaDTORespota atualizarDespesaDinamicamente(final Long id, final DespesaRequisicaoDinamicaDTO dto) {
        var entidade = despesaRepository.findById(id).orElseThrow(DespesaException::despesaNaoEncontrada);

        if (dto.descricao() != null) entidade.setDescricao(dto.descricao());
        if (dto.valor() != null) entidade.setValor(dto.valor());
        if (dto.categoria() != null) entidade.setCategoria(CategoriaDespesaEnum.valueOf(dto.categoria()));
        if (dto.formaPagamento() != null) entidade.setFormaPagamento(FormaPagamentoEnum.valueOf(dto.formaPagamento()));
        if (dto.dataPrevistaPagamento() != null) entidade.setDataPrevistaPagamento(dto.dataPrevistaPagamento());
        if (dto.dataPagamento() != null) entidade.setDataPagamento(dto.dataPagamento());
        if (dto.statusDespesa() != null) entidade.setStatusDespesa(StatusDespesaEnum.valueOf(dto.statusDespesa()));

        return despesaMapper.mapearParaDTO(entidade);
    }
}