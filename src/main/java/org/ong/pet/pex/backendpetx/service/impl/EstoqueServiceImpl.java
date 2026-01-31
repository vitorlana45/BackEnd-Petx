package org.ong.pet.pex.backendpetx.service.impl;

import org.ong.pet.pex.backendpetx.controllers.estoque.EstoqueDTO;
import org.ong.pet.pex.backendpetx.dto.response.ProdutoDTOResposta;
import org.ong.pet.pex.backendpetx.dto.response.RacaoDisponivelResposta;
import org.ong.pet.pex.backendpetx.entities.*;
import org.ong.pet.pex.backendpetx.enums.PorteEnum;
import org.ong.pet.pex.backendpetx.enums.TipoProduto;
import org.ong.pet.pex.backendpetx.enums.UnidadeDeMedidaEnum;
import org.ong.pet.pex.backendpetx.repositories.ConsumoAlimentoRepository;
import org.ong.pet.pex.backendpetx.repositories.EstoqueRepository;
import org.ong.pet.pex.backendpetx.repositories.OngRepository;
import org.ong.pet.pex.backendpetx.repositories.ProdutoRepository;
import org.ong.pet.pex.backendpetx.service.EstoqueService;
import org.ong.pet.pex.backendpetx.service.mappers.EstoqueMapper;
import org.ong.pet.pex.backendpetx.service.mappers.ProdutoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
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

//        logger.info("filtrando todos os produtos de ração por QUILO");
//        double quantidadeRacaoKg = pegarEstoque.getProdutos().stream()
//                .filter(x -> x.getTipoProduto() == TipoProduto.RACAO && x.getUnidadeDeMedida() == UnidadeDeMedidaEnum.QUILO)
//                .mapToDouble(Produto::getQuantidade)
//                .sum();
//
//        logger.info("filtrando todos os produtos de ração por LITRO");
//        double quantidadeRacaoLitro = pegarEstoque.getProdutos().stream()
//                .filter(x -> x.getTipoProduto() == TipoProduto.RACAO && x.getUnidadeDeMedida() == UnidadeDeMedidaEnum.LITRO)
//                .mapToDouble(Produto::getQuantidade)
//                .sum();
//
//        logger.info("calculando a quantidade total de ração em gramas");
//        double quantidadeRacaoTotalEmGramas = (quantidadeRacaoKg * 1000) + quantidadeRacaoLitro; // 1kg = 1000g, 1L = 1L

        logger.info("calculando o consumo diário total");
        double consumoDiarioTotal = porteContagem.entrySet().stream()
                .mapToDouble(entry -> consumoDiarioPorPorte.getOrDefault(entry.getKey(), 0.0) * entry.getValue())
                .sum();

//        int diasDisponiveis = consumoDiarioTotal > 0 ? (int) (quantidadeRacaoTotalEmGramas / consumoDiarioTotal) : 0;
//        logger.info("calculando os dias disponíveis: {} dias", diasDisponiveis);

        // Log dos resultados
//        logger.info("Quantidade total de ração no estoque (em g): {}", quantidadeRacaoTotalEmGramas);
        logger.info("Consumo diário total (em g): {}", consumoDiarioTotal);
//        logger.info("Dias de ração disponíveis: {}", diasDisponiveis);

        return new RacaoDisponivelResposta(1, consumoDiarioTotal, 1);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<ProdutoDTOResposta> paginarProdutoEstoque(TipoProduto tipoProduto, String nome,
                                                          Double quantidade, UnidadeDeMedidaEnum medida,
                                                          String chave,
                                                          Pageable pageable) {

        var listaProdutos = produtoRepository.findAllProdutos(tipoProduto != null ? tipoProduto.name() : null,
                nome, quantidade, medida != null ? medida.name() : null, pageable);


        List<ProdutoDTOResposta> listaProdutosDto = produtoMapper.mapearListaProdutoParaDto(listaProdutos.getContent());

        return new PageImpl<>(listaProdutosDto, pageable, listaProdutos.getTotalElements());
    }

    // TODO: rever se continuamos com 2 descricoes
    @Transactional
    public Estoque criarEstoque(EstoqueDTO dto) {
        var estoque = new Estoque();
        estoque.setNome(dto.getNome());
        estoque.setDescricao(dto.getDescricao());

            var ong = ongRepository.findById(1L)
                    .orElseThrow(() -> new IllegalArgumentException("ONG não encontrada"));
            estoque.setOng(ong);


        String nomeCat = (dto.getCategoriaPrincipalNome() == null || dto.getCategoriaPrincipalNome().isBlank())
                ? "Geral"
                : dto.getCategoriaPrincipalNome().trim();

        var cat = new CategoriaEstoque();
        cat.setNome(nomeCat);

        // helper que seta ambos os lados
        estoque.addCategoria(cat);

        return estoqueRepository.save(estoque);
    }

    @Transactional
    public Estoque atualizarEstoque(EstoqueDTO dto) {
        var estoque = estoqueRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Estoque não encontrado"));

        estoque.setNome(dto.getNome());

        // se quiser permitir alterar ONG:

            var ong = ongRepository.findById(1L)
                    .orElseThrow(() -> new IllegalArgumentException("ONG não encontrada"));
            estoque.setOng(ong);


        // regra: não criar nova categoria na atualização automaticamente,
        // a menos que você deseje isso explicitamente:
        if (dto.getCategoriaPrincipalNome() != null && !dto.getCategoriaPrincipalNome().isBlank()) {
            var nova = new CategoriaEstoque();
            nova.setNome(dto.getCategoriaPrincipalNome().trim());
            nova.setDescricao(dto.getCategoriaPrincipalDescricao());
            estoque.addCategoria(nova);
        }

        return estoqueRepository.save(estoque);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProdutoDTOResposta> listarProdutosPorEstoque(Long estoqueId, String nome, Pageable pageable) {
        logger.info("Listando produtos do estoque ID: {}", estoqueId);

        // Aqui você pode criar uma consulta específica no repositório para filtrar por estoqueId
        // Por enquanto, vamos fazer um filtro simples com os dados existentes

        var produtos = produtoRepository.findAllProdutos(null, nome, null, null, pageable);

        // Filtrar apenas produtos do estoque especificado
        // Em uma implementação real, isso seria feito diretamente na consulta SQL
        List<ProdutoDTOResposta> produtosDoEstoque = produtoMapper.mapearListaProdutoParaDto(
                produtos.getContent().stream()
                        .filter(p -> p.getEstoque() != null && p.getEstoque().getId().equals(estoqueId))
                        .toList()
        );

        return new PageImpl<>(produtosDoEstoque, pageable, produtosDoEstoque.size());
    }
}
