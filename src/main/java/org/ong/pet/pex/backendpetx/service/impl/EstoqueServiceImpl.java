package org.ong.pet.pex.backendpetx.service.impl;

import org.ong.pet.pex.backendpetx.dto.response.ListarEstoqueResponse;
import org.ong.pet.pex.backendpetx.dto.response.RacaoDisponivelResposta;
import org.ong.pet.pex.backendpetx.entities.Animal;
import org.ong.pet.pex.backendpetx.entities.Estoque;
import org.ong.pet.pex.backendpetx.entities.Ong;
import org.ong.pet.pex.backendpetx.entities.Produto;
import org.ong.pet.pex.backendpetx.enums.PorteEnum;
import org.ong.pet.pex.backendpetx.enums.TipoProduto;
import org.ong.pet.pex.backendpetx.enums.UnidadeDeMedidaEnum;
import org.ong.pet.pex.backendpetx.repositories.EstoqueRepository;
import org.ong.pet.pex.backendpetx.repositories.OngRepository;
import org.ong.pet.pex.backendpetx.service.EstoqueService;
import org.ong.pet.pex.backendpetx.service.exceptions.EstoqueException;
import org.ong.pet.pex.backendpetx.service.exceptions.PetXException;
import org.ong.pet.pex.backendpetx.service.mappers.ProdutoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class EstoqueServiceImpl implements EstoqueService {

    private final static Logger logger = LoggerFactory.getLogger(EstoqueServiceImpl.class);

    private final EstoqueRepository estoqueRepository;
    private final OngRepository ongRepository;
    private final ProdutoMapper produtoMapper;

    private final static Long ONG = 1L;

    public EstoqueServiceImpl(EstoqueRepository estoqueRepository, OngRepository ongRepository, ProdutoMapper produtoMapper) {
        this.estoqueRepository = estoqueRepository;
        this.ongRepository = ongRepository;
        this.produtoMapper = produtoMapper;
    }


    @Override
    @Transactional(readOnly = true)
    public Estoque pegarEstoquePorId() {
        return null;
    }


    @Override
    public void salvarEstoque(CadastrarEstoqueRequisicao cadastrarEstoqueRequisicao) {

    }

    @Override
    public Estoque deletar(Long id) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public ListarEstoqueResponse listarEstoque() {

        var estoque = ongRepository.findById(ONG).orElseThrow(PetXException::ongNaoEncontrada).getEstoque();
        if(estoque.getProduto().isEmpty()) {
             throw EstoqueException.estoqueNaoContemProdutosCadastrados();
        }

        return  ListarEstoqueResponse.builder()
                .id(estoque.getId())
                .craidoEm(estoque.getCriadoEm())
                .atualizadoEm(estoque.getAtualizadoEm())
                .produtos(produtoMapper.mapearListaProdutoParaDto(estoque.getProduto()))
                .build();
    }
}
