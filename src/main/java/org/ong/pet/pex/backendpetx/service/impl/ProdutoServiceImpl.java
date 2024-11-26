package org.ong.pet.pex.backendpetx.service.impl;

import lombok.AllArgsConstructor;
import org.ong.pet.pex.backendpetx.dto.request.ProdutoDTO;
import org.ong.pet.pex.backendpetx.entities.Estoque;
import org.ong.pet.pex.backendpetx.entities.Ong;
import org.ong.pet.pex.backendpetx.entities.Produto;
import org.ong.pet.pex.backendpetx.repositories.EstoqueRepository;
import org.ong.pet.pex.backendpetx.repositories.OngRepository;
import org.ong.pet.pex.backendpetx.repositories.ProdutoRepository;
import org.ong.pet.pex.backendpetx.service.ProdutoService;
import org.ong.pet.pex.backendpetx.service.exceptions.PetXException;
import org.ong.pet.pex.backendpetx.service.mappers.ProdutoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final OngRepository ongRepository;
    private final EstoqueRepository estoqueRepository;
    private final ProdutoMapper produtoMapper;

    private final static Long ONG = 1L;

    @Transactional
    public void cadastrarProduto(ProdutoDTO dto) {
        Ong ong = ongRepository.findById(ONG).orElseThrow(PetXException::ongNaoEncontrada);
//        var estoque = estoqueRepository.findOngId(ong.getId())
//                .orElse(this.criarEstoque(ong));

        if(ong.getEstoque() == null){
            criarEstoque(ong);
        }

//        if (dto.tipoProduto() == TipoProduto.MEDICAMENTO){
//            dto.metaData().stream().filter(
//                    info -> {
//                        info.type() == "data_vencimento";
//                    }
//            );
//        }

        var produto = produtoMapper.mapearParaEntidade(dto, ong.getEstoque());
        produtoRepository.save(produto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoDTO> listarProdutos() {
        var produtos = produtoRepository.findAll();
        return produtoMapper.meparParaDto(produtos);
    }


    @Override
    public Produto buscarProdutoPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> PetXException.produtoNaoEncontrado(id.toString()));
    }


    @Override
    public ProdutoDTO atualizarProduto(Long id, ProdutoDTO dto) {
        Produto produtoExistente = produtoRepository.findById(id)
                .orElseThrow(() -> PetXException.produtoNaoEncontrado(id.toString()));

//        var tipoProduto = ProdutoUpdateUtils.retornaInstanciaProduto(dto);
//
//        System.out.println(tipoProduto);
//
//        var v = produtoRepository.save(produtoExistente);
        return null;
    }



    @Override
    public void excluirProduto(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> PetXException.produtoNaoEncontrado(id.toString()));

        produtoRepository.delete(produto);
    }

    private Ong criarEstoque(final Ong ong) {
        Estoque estoque = new Estoque();
        estoque.setOng(ong);
        Estoque savedEstoque = estoqueRepository.save(estoque);
        ong.setEstoque(savedEstoque);
        return ongRepository.save(ong);
    }

}