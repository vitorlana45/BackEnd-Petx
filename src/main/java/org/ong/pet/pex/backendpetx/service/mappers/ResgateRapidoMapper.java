package org.ong.pet.pex.backendpetx.service.mappers;

import org.ong.pet.pex.backendpetx.dto.request.AnimalGenericoRequisicao;
import org.ong.pet.pex.backendpetx.dto.request.BoletimRequisicao;
import org.ong.pet.pex.backendpetx.dto.request.ResgateRapidoDTO;
import org.ong.pet.pex.backendpetx.enums.Destino;

/**
 * Classe utilitária para conversão do ResgateRapidoDTO para BoletimRequisicao
 */
public class ResgateRapidoMapper {
    
    /**
     * Converte um ResgateRapidoDTO para um BoletimRequisicao
     * permitindo um fluxo simplificado de cadastro
     * 
     * @param resgateRapido DTO com informações básicas do resgate
     * @return BoletimRequisicao preenchido para salvar
     */
    public static BoletimRequisicao converterParaBoletimDTO(ResgateRapidoDTO resgateRapido) {
        // Criar o DTO de boletim
        BoletimRequisicao boletimDTO = new BoletimRequisicao();
        
        // Preencher dados do resgate
        boletimDTO.setDataAtendimento(resgateRapido.getDataResgate());
        boletimDTO.setOrigem(resgateRapido.getOrigem());
        boletimDTO.setMotivoRecolhimento("Resgate rápido");
        boletimDTO.setObservacaoClinica(resgateRapido.getObservacoes());
        
        // Endereço
        boletimDTO.setRuaAvenida(resgateRapido.getLocalResgate());
        boletimDTO.setCidade(resgateRapido.getCidadeResgate());
        
        // Destino padrão
        boletimDTO.setDestino(Destino.ABRIGO);
        
        // Criar dados do animal
        AnimalGenericoRequisicao animalDTO = new AnimalGenericoRequisicao();
        animalDTO.setNome(resgateRapido.getNome());
        animalDTO.setEspecie(resgateRapido.getEspecieEnum());
        animalDTO.setMaturidade(resgateRapido.getIdadeAproximada());
        animalDTO.setSexo(resgateRapido.getSexo());
        animalDTO.setCorPelagem(resgateRapido.getCor());
        animalDTO.setPorte(resgateRapido.getPorte());
        animalDTO.setSaude(resgateRapido.getStatusSaude());
        animalDTO.setRaca("SRD"); // Padrão para cadastro rápido
        animalDTO.setOrigem(resgateRapido.getOrigem());
        animalDTO.setDestino(Destino.ABRIGO);
        animalDTO.setComportamento("A avaliar");
        animalDTO.setAnimalEMaezinha(false);
        animalDTO.setCondicaoAnimal("Resgate rápido - avaliação pendente");
        
        // Adicionar animal ao boletim
        boletimDTO.setAnimal(animalDTO);
        
        return boletimDTO;
    }
}
