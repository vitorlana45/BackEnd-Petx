package org.ong.pet.pex.backendpetx.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ong.pet.pex.backendpetx.dto.request.BoletimDTORequisicao;
import org.ong.pet.pex.backendpetx.dto.request.ResgateRapidoDTO;
import org.ong.pet.pex.backendpetx.dto.response.BoletimDTOResposta;
import org.ong.pet.pex.backendpetx.service.BoletimService;
import org.ong.pet.pex.backendpetx.service.mappers.ResgateRapidoMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

/**
 * Controller para gerenciar as operações relacionadas aos resgates de animais.
 */
@Controller
@RequestMapping("/resgates")
@RequiredArgsConstructor
@Slf4j
public class ResgateController {

    private final BoletimService boletimService;

    /**
     * Exibe a página inicial de resgates com listagem e estatísticas.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VETERINARIO', 'OPERADOR')")
    public String index(Model model, Pageable pageable) {
        // Busca os resgates mais recentes (últimos 10)
        Page<BoletimDTOResposta> resgatesRecentes = boletimService.findAllBoletins(null, null, 
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "data_atendimento")));
        
        // Busca todos os resgates (com paginação)
        Page<BoletimDTOResposta> todosResgates = boletimService.findAllBoletins(
                null, null, PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "data_atendimento")));
        
        model.addAttribute("resgatesRecentes", resgatesRecentes.getContent());
        model.addAttribute("todosResgates", todosResgates.getContent());
        
        return "resgates/index";
    }

    /**
     * Exibe o formulário para cadastro rápido de animal resgatado.
     */
    @GetMapping("/rapido")
    @PreAuthorize("hasAnyRole('ADMIN', 'VETERINARIO', 'OPERADOR')")
    public String formResgaterapido(Model model) {
        // Inicializa o DTO com a data atual
        ResgateRapidoDTO dto = new ResgateRapidoDTO();
        dto.setDataResgate(LocalDateTime.now());
        dto.setTipoResgate("RAPIDO");
        
        model.addAttribute("resgateRapidoDTO", dto);
        return "resgates/rapido";
    }
    
    /**
     * Processa o formulário de cadastro rápido e salva o animal através do boletim.
     */
    @PostMapping("/salvar")
    @PreAuthorize("hasAnyRole('ADMIN', 'VETERINARIO', 'OPERADOR')")
    public String salvarResgateRapido(
            @ModelAttribute("resgateRapidoDTO") ResgateRapidoDTO dto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            log.error("Erro na validação do formulário: {}", result.getAllErrors());
            return "resgates/rapido";
        }
        
        try {
            // Converter para BoletimDTORequisicao
            BoletimDTORequisicao boletimDTO = ResgateRapidoMapper.converterParaBoletimDTO(dto);
            
            // Salvar o boletim
            BoletimDTOResposta boletimSalvo = boletimService.createBoletim(boletimDTO);
            
            redirectAttributes.addFlashAttribute("mensagemSucesso", 
                    "Resgate cadastrado com sucesso! Número do boletim: " + boletimSalvo.getNumeroOcorrencia());
            
            return "redirect:/resgates";
            
        } catch (Exception e) {
            log.error("Erro ao salvar resgate rápido", e);
            redirectAttributes.addFlashAttribute("mensagemErro", 
                    "Erro ao cadastrar resgate: " + e.getMessage());
            return "redirect:/resgates/rapido";
        }
    }
}
