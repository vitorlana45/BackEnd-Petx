package org.ong.pet.pex.backendpetx.service.mappers;

import lombok.AllArgsConstructor;
import org.ong.pet.pex.backendpetx.dto.request.DespesaRequisicaoDTO;
import org.ong.pet.pex.backendpetx.dto.response.DespesaDTORespota;
import org.ong.pet.pex.backendpetx.dto.response.ListarDespesaResposta;
import org.ong.pet.pex.backendpetx.entities.Despesa;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class DespesaMapper {

    public DespesaDTORespota mapearParaDTO(Despesa despesa) {
        return DespesaDTORespota.builder()
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

    public Despesa mapearParaEntidade(DespesaRequisicaoDTO dto) {
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