package org.ong.pet.pex.backendpetx.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ong.pet.pex.backendpetx.dto.request.BoletimRequisicao;
import org.ong.pet.pex.backendpetx.dto.request.ResgateRapidoDTO;
import org.ong.pet.pex.backendpetx.dto.response.BoletimResposta;
import org.ong.pet.pex.backendpetx.enums.Destino;
import org.ong.pet.pex.backendpetx.enums.SaudeEnum;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Map;

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
    @GetMapping(name = "RESGATES#LISTAR")
    @PreAuthorize("hasAnyRole('ADMIN', 'VETERINARIO', 'OPERADOR')")
    public String index(Model model,
                        Pageable pageable,
                        @org.springframework.web.bind.annotation.RequestParam(value = "numero", required = false) Long numeroOcorrencia,
                        @org.springframework.web.bind.annotation.RequestParam(value = "destino", required = false) Destino destino,
                        @org.springframework.web.bind.annotation.RequestParam(value = "tab", required = false) String tab,
                        @org.springframework.web.bind.annotation.RequestParam(value = "meses", required = false, defaultValue = "6") Integer meses) {
    // Página de recentes fixa (não paginada pelo usuário) - últimos 10
    // Ajuste: usar nome de propriedade da entidade (dataAtendimento) em vez de snake_case
    Page<BoletimResposta> resgatesRecentes = boletimService.findAllBoletins(null, null,
        PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "dataAtendimento")));

        // Determina sort solicitado (whitelist para evitar campos inválidos / injection)
    Sort sort = pageable != null && pageable.getSort().isSorted()
        ? sanitizeSort(pageable.getSort())
        : Sort.by(Sort.Direction.DESC, "dataAtendimento");

        int pageNumber = (pageable == null) ? 0 : pageable.getPageNumber();
    // Força padrão de 10 itens se não especificado ou se tamanho inválido (<1)
    int pageSize = (pageable == null || pageable.getPageSize() <= 0) ? 10 : pageable.getPageSize();

        Pageable effectivePageable = PageRequest.of(pageNumber, pageSize, sort);

    Page<BoletimResposta> todosResgatesPage = boletimService.findAllBoletins(numeroOcorrencia, destino, effectivePageable);

    model.addAttribute("resgatesRecentes", resgatesRecentes.getContent());
    model.addAttribute("todosResgatesPage", todosResgatesPage);
    model.addAttribute("destinoFiltro", destino);
    model.addAttribute("numeroFiltro", numeroOcorrencia);
    model.addAttribute("origens", Destino.values());
    model.addAttribute("statusList", SaudeEnum.values()); // placeholder para futura filtragem por status

    // Determina aba ativa: prioridade para parâmetro explícito; senão inferir se há filtros/paginação
    String activeTab = (tab != null && !tab.isBlank()) ? tab : "recentes";
    if (tab == null) {
        boolean hasFilters = (numeroOcorrencia != null) || (destino != null);
        boolean paginatingTodos = pageable != null && pageable.getPageNumber() > 0; // usuário navegou páginas da aba 'todos'
        if (hasFilters || paginatingTodos) {
            activeTab = "todos";
        }
    }
    model.addAttribute("activeTab", activeTab);
    try {
        int mesesValidado = (meses == null || meses < 1 || meses > 24) ? 6 : meses; // limite máximo 24
        var estatisticas = boletimService.obterEstatisticasResgates(mesesValidado);
        model.addAttribute("estatisticas", estatisticas);
        model.addAttribute("estatisticasCarregadas", true);
        model.addAttribute("mesesSelecionados", mesesValidado);
    } catch (Exception ex) {
        log.warn("Falha ao carregar estatísticas de resgates", ex);
        model.addAttribute("estatisticasCarregadas", false);
    }

        // Infos de sort para o template
    Sort.Order primary = sort.stream().findFirst().orElse(Sort.Order.desc("dataAtendimento"));
        model.addAttribute("currentSortField", primary.getProperty());
        model.addAttribute("currentSortDir", primary.getDirection().name().toLowerCase());
        model.addAttribute("nextSortDir", primary.getDirection().isAscending() ? "desc" : "asc");

    return "resgates/index";
    }

    /**
     * Endpoint AJAX para estatísticas (JSON) usado pelo front para evitar recarregar toda a página e permitir melhorias.
     */
    @GetMapping(value = "/api/estatisticas", name = "RESGATES#ESTATISTICAS_API")
    @PreAuthorize("hasAnyRole('ADMIN', 'VETERINARIO', 'OPERADOR')")
    @ResponseBody
    public Map<String,Object> estatisticasAjax(@org.springframework.web.bind.annotation.RequestParam(value = "meses", required = false, defaultValue = "6") Integer meses) {
        int mesesValidado = (meses == null || meses < 1 || meses > 24) ? 6 : meses;
        return boletimService.obterEstatisticasResgates(mesesValidado);
    }

    private Sort sanitizeSort(Sort requested) {
        // Campos permitidos (nomes de propriedades da entidade Boletim)
        String[] allowed = {"numeroOcorrencia", "dataAtendimento", "destino"};
        java.util.List<String> allowedList = java.util.Arrays.asList(allowed);

        // Função de mapeamento snake_case -> camelCase conhecida
        java.util.function.Function<String,String> mapProp = p -> {
            if(p == null) return null;
            return switch (p) {
                case "data_atendimento" -> "dataAtendimento";
                case "numero_ocorrencia" -> "numeroOcorrencia";
                default -> p;
            };
        };

        Sort.Order effective = requested.stream()
                .map(o -> new Sort.Order(o.getDirection(), mapProp.apply(o.getProperty())))
                .filter(o -> allowedList.contains(o.getProperty()))
                .findFirst()
                .orElse(Sort.Order.desc("dataAtendimento"));

        return Sort.by(effective);
    }

    /**
     * Exibe o formulário para cadastro rápido de animal resgatado.
     */
    @GetMapping(value = "/rapido", name = "RESGATES#RAPIDO")
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
    @PostMapping(value = "/salvar", name = "RESGATES#SALVAR")
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
            // Converter para BoletimRequisicao
            BoletimRequisicao boletimDTO = ResgateRapidoMapper.converterParaBoletimDTO(dto);
            
            // Salvar o boletim
            BoletimResposta boletimSalvo = boletimService.createBoletim(boletimDTO);
            
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
