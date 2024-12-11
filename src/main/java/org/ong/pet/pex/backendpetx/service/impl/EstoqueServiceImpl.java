package org.ong.pet.pex.backendpetx.service.impl;

import org.ong.pet.pex.backendpetx.dto.response.ProdutoDTOResposta;
import org.ong.pet.pex.backendpetx.dto.response.RacaoDisponivelResposta;
import org.ong.pet.pex.backendpetx.entities.Animal;
import org.ong.pet.pex.backendpetx.entities.ConsumoAlimento;
import org.ong.pet.pex.backendpetx.entities.Ong;
import org.ong.pet.pex.backendpetx.entities.Produto;
import org.ong.pet.pex.backendpetx.enums.PorteEnum;
import org.ong.pet.pex.backendpetx.enums.TipoProduto;
import org.ong.pet.pex.backendpetx.enums.UnidadeDeMedidaEnum;
import org.ong.pet.pex.backendpetx.repositories.ConsumoAlimentoRepository;
import org.ong.pet.pex.backendpetx.repositories.EstoqueRepository;
import org.ong.pet.pex.backendpetx.repositories.OngRepository;
import org.ong.pet.pex.backendpetx.repositories.ProdutoRepository;
import org.ong.pet.pex.backendpetx.service.EstoqueService;
import org.ong.pet.pex.backendpetx.service.impl.especificacao.ConstrutorDeEspecificacaoProduto;
import org.ong.pet.pex.backendpetx.service.mappers.EstoqueMapper;
import org.ong.pet.pex.backendpetx.service.mappers.ProdutoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class EstoqueServiceImpl implements EstoqueService {

    private final static Logger logger = LoggerFactory.getLogger(EstoqueServiceImpl.class);

    private final EstoqueRepository estoqueRepository;
    private final OngRepository ongRepository;
    private final ProdutoRepository produtoRepository;
    private final static Long ONG = 1L;
    private final EstoqueMapper estoqueMapper;
    private final ProdutoMapper produtoMapper;
    private final ConsumoAlimentoRepository consumoAlimentoRepository;

    public EstoqueServiceImpl(EstoqueRepository estoqueRepository, OngRepository ongRepository, ProdutoRepository produtoRepository, EstoqueMapper estoqueMapper, ProdutoMapper produtoMapper, ConsumoAlimentoRepository consumoAlimentoRepository) {
        this.estoqueRepository = estoqueRepository;
        this.ongRepository = ongRepository;
        this.produtoRepository = produtoRepository;
        this.estoqueMapper = estoqueMapper;
        this.produtoMapper = produtoMapper;
        this.consumoAlimentoRepository = consumoAlimentoRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public RacaoDisponivelResposta calcularQuantidadeRacao() {

        Map<PorteEnum, Double> consumoDiarioPorPorte = new HashMap<>();

        List<ConsumoAlimento> todosConsumos = consumoAlimentoRepository.findAll();

        todosConsumos.forEach(consumo -> {
            PorteEnum porte = consumo.getPorte();
            Double consumoDiario = consumo.getConsumoDiario();
            consumoDiarioPorPorte.put(porte, consumoDiario);
        });

        // Obter os animais da ONG
        Ong ong = ongRepository.findById(ONG).orElseThrow();
        Set<Animal> animais = ong.getAnimais();

        // Contagem de animais por porte
        Map<PorteEnum, Integer> porteContagem = new HashMap<>();
        animais.forEach(animal -> {
            PorteEnum porte = animal.getPorteEnum();
            porteContagem.put(porte, porteContagem.getOrDefault(porte, 0) + 1);
        });

        logger.info("buscando pelo estoque com base na ONG");
        var pegarEstoque = ong.getEstoque();

        logger.info("filtrando todos os produtos de ração por QUILO");
        double quantidadeRacaoKg = pegarEstoque.getProdutos().stream()
                .filter(x -> x.getTipoProduto() == TipoProduto.RACAO && x.getUnidadeDeMedida() == UnidadeDeMedidaEnum.QUILO)
                .mapToDouble(Produto::getQuantidade)
                .sum();

        logger.info("filtrando todos os produtos de ração por LITRO");
        double quantidadeRacaoLitro = pegarEstoque.getProdutos().stream()
                .filter(x -> x.getTipoProduto() == TipoProduto.RACAO && x.getUnidadeDeMedida() == UnidadeDeMedidaEnum.LITRO)
                .mapToDouble(Produto::getQuantidade)
                .sum();

        logger.info("calculando a quantidade total de ração em gramas");
        double quantidadeRacaoTotalEmGramas = (quantidadeRacaoKg * 1000) + quantidadeRacaoLitro; // 1kg = 1000g, 1L = 1L

        logger.info("calculando o consumo diário total");
        double consumoDiarioTotal = porteContagem.entrySet().stream()
                .mapToDouble(entry -> consumoDiarioPorPorte.getOrDefault(entry.getKey(), 0.0) * entry.getValue())
                .sum();

        int diasDisponiveis = consumoDiarioTotal > 0 ? (int) (quantidadeRacaoTotalEmGramas / consumoDiarioTotal) : 0;
        logger.info("calculando os dias disponíveis: {} dias", diasDisponiveis);

        // Log dos resultados
        logger.info("Quantidade total de ração no estoque (em g): {}", quantidadeRacaoTotalEmGramas);
        logger.info("Consumo diário total (em g): {}", consumoDiarioTotal);
        logger.info("Dias de ração disponíveis: {}", diasDisponiveis);

        return new RacaoDisponivelResposta(quantidadeRacaoTotalEmGramas, consumoDiarioTotal, diasDisponiveis);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<ProdutoDTOResposta> paginarProdutoEstoque(TipoProduto tipoProduto, String nome,
                                                          Double quantidade, UnidadeDeMedidaEnum medida,
                                                          String chave, String valor,
                                                          Pageable pageable) {

        Specification<Produto> especificacaoProduto = new ConstrutorDeEspecificacaoProduto()
                .adicionarFiltroPorAtributosEspecificos(chave, valor)
                .adicionarFiltroEnum("tipoProduto", tipoProduto)
                .adicionarFiltroStringExata("nome", nome)
                .build();

        Page<Produto> listaProdutos = produtoRepository.findAll(especificacaoProduto, pageable);

        List<ProdutoDTOResposta> listaProdutosDto = produtoMapper.mapearListaProdutoParaDto(listaProdutos);

        return new PageImpl<>(listaProdutosDto, pageable, listaProdutos.getTotalElements());
    }

}
