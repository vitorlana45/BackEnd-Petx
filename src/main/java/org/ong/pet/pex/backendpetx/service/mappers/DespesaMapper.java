package org.ong.pet.pex.backendpetx.service.mappers;

import lombok.AllArgsConstructor;
import org.ong.pet.pex.backendpetx.dto.request.DespesaRequisicao;
import org.ong.pet.pex.backendpetx.dto.response.DespesaResposta;
import org.ong.pet.pex.backendpetx.dto.response.ListarDespesaResposta;
import org.ong.pet.pex.backendpetx.entity.Despesa;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class DespesaMapper {

    public DespesaResposta mapearParaDTO(Despesa despesa) {
        return DespesaResposta.builder()
                .id(despesa.getId())
                .descricao(despesa.getDescricao())
                .valor(despesa.getValor())
                .categoria(despesa.getCategoria())
                .dataPrevistaPagamento(despesa.getDataPrevistaPagamento())
                .dataPagamento(despesa.getDataPagamento())
                .statusDespesa(despesa.getStatusDespesa())
                .formaPagamento(despesa.getFormaPagamento())
                .build();
    }

    public Despesa mapearParaEntidade(DespesaRequisicao dto) {
        return Despesa.builder()
                .descricao(dto.descricao())
                .valor(dto.valor())
                .categoria(dto.categoria())
                .formaPagamento(dto.formaPagamento())
                .dataPrevistaPagamento(dto.dataPrevistaPagamento())
                .dataPagamento(dto.dataPagamento())
                .statusDespesa(dto.statusDespesa())
                .build();
    }

    public List<ListarDespesaResposta> mapearListaParaDTO(List<Despesa> despesas) {
        return despesas.stream()
                .map(despesa -> ListarDespesaResposta.builder()
                        .id(despesa.getId())
                        .descricao(despesa.getDescricao())
                        .valor(despesa.getValor())
                        .categoria(despesa.getCategoria())
                        .dataPrevistaPagamento(despesa.getDataPrevistaPagamento())
                        .dataPagamento(despesa.getDataPagamento())
                        .statusDespesa(despesa.getStatusDespesa())
                        .formaPagamento(despesa.getFormaPagamento())
                        .build())
                .toList();
    }
}