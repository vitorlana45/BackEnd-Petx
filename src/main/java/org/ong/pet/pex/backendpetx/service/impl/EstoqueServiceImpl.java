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

    @Transactional(readOnly = true)
    @Override
    public RacaoDisponivelResposta calcularQuantidadeRacao() {

        // Definição dos portes
        Enum<PorteEnum> portePequeno = PorteEnum.PEQUENO;
        Enum<PorteEnum> porteMedio = PorteEnum.MEDIO;
        Enum<PorteEnum> porteGrande = PorteEnum.GRANDE;

        // Consumo diário por animal de cada porte em gramas
        int racaoPequeno = 100;  // 100g por dia
        int racaoMedio = 250;    // 250g por dia
        int racaoGrande = 350;   // 350g por dia

        // Obter os animais da ONG
        Ong ong = ongRepository.findById(ONG).orElseThrow();
        Set<Animal> animais = ong.getAnimais();

        // Contagem de animais por porte usando array
        int[] porteCounts = new int[3];

        logger.info("inciando contagem de animais por porte");
        animais.forEach(animal -> {
            if (animal.getPorteEnum().equals(portePequeno)) {
                porteCounts[0]++;
            } else if (animal.getPorteEnum().equals(porteMedio)) {
                porteCounts[1]++;
            } else if (animal.getPorteEnum().equals(porteGrande)) {
                porteCounts[2]++;
            }
        });

        logger.info("buscando pelo estoque com base na ONG");
        var pegarEstoque = ong.getEstoque();

        logger.info("filtrando todos os produtos de ração por QUILO");
        long quantidadeRacaoKg = pegarEstoque.getProduto().stream()
                .filter(x -> x.getTipoProduto() == TipoProduto.RACAO && x.getUnidadeDeMedida() == UnidadeDeMedidaEnum.QUILO)
                .mapToLong(Produto::getQuantidade)
                .sum();

        logger.info("filtrando todos os produtos de ração por LITRO");
        long quantidadeRacaoLitro = pegarEstoque.getProduto().stream()
                .filter(x -> x.getTipoProduto() == TipoProduto.RACAO && x.getUnidadeDeMedida() == UnidadeDeMedidaEnum.LITRO)
                .mapToLong(Produto::getQuantidade)
                .sum();

        logger.info("calculando a quantidade total de ração em gramas");
        long quantidadeRacaoTotalEmGramas = (quantidadeRacaoKg * 1000) + (quantidadeRacaoLitro * 1000); // 1kg = 1000g

        logger.info("calculando o consumo diário total");
        double consumoDiarioTotal = (porteCounts[0] * racaoPequeno) +
                                    (porteCounts[1] * racaoMedio) +
                                    (porteCounts[2] * racaoGrande);


        double diasDisponiveis = consumoDiarioTotal > 0 ? quantidadeRacaoTotalEmGramas / consumoDiarioTotal : 0;
        String diasDisponiveisFormatado = String.format("%.0f", diasDisponiveis);
        logger.info("calculando os dias disponíveis: {} dias",diasDisponiveisFormatado);


        // Log dos resultados
        logger.info("Quantidade total de ração no estoque (em g): {}", quantidadeRacaoTotalEmGramas);
        logger.info("Consumo diário total (em g): {}", consumoDiarioTotal);
        logger.info("Dias de ração disponíveis: {}", diasDisponiveis);

        System.out.println("Quantidade de ração (em gramas): " + quantidadeRacaoTotalEmGramas);
        System.out.println("Consumo diário total (em gramas): " + consumoDiarioTotal);
        System.out.println("Dias de ração disponíveis: " + diasDisponiveis);

        return new RacaoDisponivelResposta(quantidadeRacaoTotalEmGramas, consumoDiarioTotal, diasDisponiveisFormatado);
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
