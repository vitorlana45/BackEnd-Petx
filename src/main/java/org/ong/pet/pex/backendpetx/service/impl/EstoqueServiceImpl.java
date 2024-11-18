package org.ong.pet.pex.backendpetx.service.impl;

import org.ong.pet.pex.backendpetx.dto.request.CadastrarEstoqueRequisicao;
import org.ong.pet.pex.backendpetx.dto.response.RacaoDisponivelResposta;
import org.ong.pet.pex.backendpetx.entities.Estoque;
import org.ong.pet.pex.backendpetx.repositories.EstoqueRepository;
import org.ong.pet.pex.backendpetx.repositories.OngRepository;
import org.ong.pet.pex.backendpetx.service.EstoqueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstoqueServiceImpl implements EstoqueService {

    private final static Logger logger = LoggerFactory.getLogger(EstoqueServiceImpl.class);

    private final EstoqueRepository estoqueRepository;
    private final OngRepository ongRepository;

    public EstoqueServiceImpl(EstoqueRepository estoqueRepository, OngRepository ongRepository) {
        this.estoqueRepository = estoqueRepository;
        this.ongRepository = ongRepository;
    }


//    @Override
//    public void salvarEstoque(CadastrarEstoqueRequisicao cadastrarEstoqueRequisicao) {
//        Estoque estoque = Estoque.builder()
//                .(cadastrarEstoqueRequisicao.racao())
//                .quantidade(cadastrarEstoqueRequisicao.quantidade())
//                .especie(cadastrarEstoqueRequisicao.especie())
//                .porte(cadastrarEstoqueRequisicao.porte())
//                .build();
//        estoque.setOng(ongRepository.findById(1L).orElseThrow());
//        estoqueRepository.save(estoque);
//
//    }


    @Override
    public RacaoDisponivelResposta calcularQuantidadeRacao() {
//            // Definição dos portes
//            Enum<PorteEnum> portePequeno = PorteEnum.PEQUENO;
//            Enum<PorteEnum> porteMedio = PorteEnum.MEDIO;
//            Enum<PorteEnum> porteGrande = PorteEnum.GRANDE;
//
//            // Consumo diário por animal de cada porte em gramas
//            int racaoPequeno = 100;  // 100g por dia
//            int racaoMedio = 250;    // 250g por dia
//            int racaoGrande = 350;   // 350g por dia
//
//            // Obter os animais da ONG
//            Ong ong = ongRepository.findById(1L).orElseThrow();
//            Set<Animal> animais = ong.getAnimais();
//
//            // Contagem de animais por porte usando array
//            int[] porteCounts = new int[3];
//
//            // Contagem de animais por porte
//            animais.forEach(animal -> {
//                if (animal.getPorteEnum().equals(portePequeno)) {
//                    porteCounts[0]++;
//                } else if (animal.getPorteEnum().equals(porteMedio)) {
//                    porteCounts[1]++;
//                } else if (animal.getPorteEnum().equals(porteGrande)) {
//                    porteCounts[2]++;
//                }
//            });
//
//            // Calcular a quantidade total de ração no estoque
//            double quantidadeRacaoNoEstoque = estoqueRepository.findAll().stream()
//                    .mapToDouble(Estoque::getQuantidade)  // Assume que quantidade já está em gramas
//                    .sum();
//
//            // Calcular o consumo diário total de todos os animais
//            double consumoDiarioTotal = (porteCounts[0] * racaoPequeno) +
//                                     (porteCounts[1] * racaoMedio) +
//                                     (porteCounts[2] * racaoGrande);
//
//            // Calcular dias disponíveis
//            double diasDisponiveis = consumoDiarioTotal > 0
//                    ? quantidadeRacaoNoEstoque / consumoDiarioTotal
//                    : 0;
//
//            // Log dos resultados
//            logger.info("Quantidade total de ração no estoque (em g): {}", quantidadeRacaoNoEstoque);
//            logger.info("Consumo diário total (em g): {}", consumoDiarioTotal);
//            logger.info("Dias de ração disponíveis: {}", diasDisponiveis);
//
//            return new RacaoDisponivelResposta(Integer.parseInt(String.valueOf(diasDisponiveis)));
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
    public List<Estoque> GetAll() {
        return List.of();
    }

    @Override
    public Estoque update(Long id, Estoque estoque) {
        return null;
    }
}
