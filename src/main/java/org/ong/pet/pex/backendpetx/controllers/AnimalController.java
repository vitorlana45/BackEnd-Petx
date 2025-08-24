package org.ong.pet.pex.backendpetx.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.ong.pet.pex.backendpetx.bean.StatsCardBean;
import org.ong.pet.pex.backendpetx.dto.request.AnimalGenericoRequisicao;
import org.ong.pet.pex.backendpetx.dto.request.AnimalObituarioResquisicao;
import org.ong.pet.pex.backendpetx.dto.response.AnimalGenericoResposta;
import org.ong.pet.pex.backendpetx.dto.response.AnimalPaginadoResposta;
import org.ong.pet.pex.backendpetx.enums.*;
import org.ong.pet.pex.backendpetx.service.AnimalService;
import org.ong.pet.pex.backendpetx.controllers.bean.ActionButtonDTO;
import org.ong.pet.pex.backendpetx.controllers.helper.SmartPageHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller para gestão de Animais
 */
@Controller
@RequestMapping("/animais")
@PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
public class AnimalController {

    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    /**
     * Lista todos os animais com paginação e filtros
     */
    @GetMapping()
    public String listarAnimais(Model model,
                                @RequestParam(required = false) String nome,
                                @RequestParam(required = false) String raca,
                                @RequestParam(required = false) EspecieEnum especie,
                                @RequestParam(required = false) PorteEnum porte,
                                @RequestParam(required = false) StatusEnum status,
                                @RequestParam(required = false) String doenca,
                                @RequestParam(required = false) String comportamento,
                                @RequestParam(required = false) MaturidadeEnum maturidade,
                                @RequestParam(required = false) OrigemAnimalEnum origem,
                                @RequestParam(required = false) SexoEnum sexo,
                                @PageableDefault(size = 12) Pageable pageable,
                                @RequestHeader(value = "HX-Request", required = false) String htmx) {

        SmartPageHelper.setupAnimalsPage(model);

        List<ActionButtonDTO> actionButtons = getActionButtonDTOS();
        model.addAttribute("actionButtons", actionButtons);

        Page<AnimalPaginadoResposta> page = animalService.paginarAnimais(
                nome, raca, especie, porte, status, doenca, comportamento, maturidade, origem, sexo, pageable
        );

        carregarComboFiltros(model, nome, raca, especie, porte, status, doenca, comportamento, maturidade, origem, sexo);

        montarComboStatusEnum(model);

        model.addAttribute("page", page);
        model.addAttribute("currentPage", "/animais");
        model.addAttribute("animais", page.getContent());
        model.addAttribute("statsCard", montarStatsCard());

        // Se a requisição veio do HTMX, devolve só o fragmento da tabela/lista
        if ("true".equalsIgnoreCase(htmx)) {
            return "animais/fragmentos";
        }

        return "animais/lista";
    }

    public List<StatsCardBean> montarStatsCard() {
        List<StatsCardBean> statsCards = new ArrayList<>();
        statsCards.add(
                new StatsCardBean("fas fa-paw","Total de Animais",
                String.valueOf(animalService.contarQuantidadeAnimais()),
                        "primary")
        );

        statsCards.add(
                new StatsCardBean("fas fa-check", "Animais Disponíveis",
                String.valueOf(10L), // animalService.contarQuantidadeAnimaisPorStatus(StatusEnum.DISPONIVEL)),
                "success")
        );

        statsCards.add(
                new StatsCardBean("fas fa-home", "Animais Adotados",
                String.valueOf(10L), // animalService.contarQuantidadeAnimaisPorStatus(StatusEnum.ADOTADO)),
               "info")
        );

        statsCards.add(
                new StatsCardBean( "fas fa-procedures", "Animais em Tratamento",
                String.valueOf(2L), // animalService.contarQuantidadeAnimaisPorStatus(StatusEnum.EM_TRATAMENTO)),
               "warning")
        );

        return statsCards;
    }



    private static List<ActionButtonDTO> getActionButtonDTOS() {
        ActionButtonDTO actionButton = ActionButtonDTO.createCustom(
                "Novo Animal",
                "/boletins/form",
                "fas fa-plus",
                "btn-primary"
        );

//        ActionButtonDTO segundo = ActionButtonDTO.createCustom(
//                "Importar Animais",
//                "/animais/importar",
//                "fas fa-file-import",
//                "btn-secondary"
//        );


        List<ActionButtonDTO> actionButtons = new ArrayList<>();
        actionButtons.add(actionButton);
//        actionButtons.add(segundo);
        return actionButtons;
    }


    /**
     * Exibe detalhes de um animal específico
     */
    @GetMapping(value = "/{id}")
    public String detalhesAnimal(@PathVariable Long id, Model model) {
        AnimalGenericoResposta animal = animalService.buscarAnimalPorId(id);
        montarComboStatusEnum(model);
        model.addAttribute("animal", animal);
        return "animais/perfil";
    }

    /**
     * Atualiza bloco PERFIL via formulário parcial (POST simples por enquanto)
     */
    @PostMapping(value = "/{id}/atualizar/perfil")
    public String atualizarPerfil(@PathVariable Long id,
                                  @RequestParam(required = false) String nome,
                                  @RequestParam(required = false) String raca,
                                  @RequestParam(required = false) String especie,
                                  @RequestParam(required = false) String porte,
                                  @RequestParam(required = false) String sexo,
                                  @RequestParam(required = false) String maturidade,
                                  @RequestParam(required = false) String origem,
                                  @RequestParam(required = false) String corPelagem,
                                  RedirectAttributes ra) {
        try {
            animalService.atualizarPerfilBasico(id, nome, raca, especie, porte, sexo, maturidade, origem, corPelagem);
            ra.addFlashAttribute("mensagemSucesso", "Perfil atualizado.");
        } catch (Exception ex) {
            ra.addFlashAttribute("mensagemErro", "Erro ao atualizar perfil: " + ex.getMessage());
        }
        return "redirect:/animais/" + id;
    }

    /**
     * Atualiza bloco SAÚDE resumida
     */
    @PostMapping(value = "/{id}/atualizar/saude")
    public String atualizarSaude(@PathVariable Long id,
                                 @RequestParam(required = false) String doencas,
                                 RedirectAttributes ra) {
        try {
            animalService.atualizarResumoSaude(id, doencas);
            ra.addFlashAttribute("mensagemSucesso", "Saúde atualizada.");
        } catch (Exception ex) {
            ra.addFlashAttribute("mensagemErro", "Erro ao atualizar saúde: " + ex.getMessage());
        }
        return "redirect:/animais/" + id;
    }

    /**
     * Endpoint JSON compacto para modal de detalhes via AJAX
     */
    @GetMapping(value = "/{id}/detalhes.json", produces = "application/json")
    @ResponseBody
    public Map<String,Object> detalhesAnimalJson(@PathVariable Long id){
        AnimalGenericoResposta animal = animalService.buscarAnimalPorId(id);
        Map<String,Object> resp = new HashMap<>();
        resp.put("id", animal.getId());
        resp.put("chipId", animal.getChipId());
        resp.put("nome", animal.getNome());
        resp.put("raca", animal.getRaca());
        resp.put("especie", animal.getEspecie());
        resp.put("porte", animal.getPorte());
        resp.put("sexo", animal.getSexo());
        resp.put("maturidade", animal.getMaturidade());
        resp.put("origem", animal.getOrigem());
        resp.put("status", animal.getStatus());
        resp.put("comportamento", animal.getComportamento());
        resp.put("doencas", animal.getDoencas());
        resp.put("corPelagem", animal.getCorPelagem());
        resp.put("condicaoAnimal", animal.getCondicaoAnimal());
        // Campos relacionados (lazy) podem ser adicionados futuramente se DTO suportar
        return resp;
    }
    
    /**
     * Busca animal por chip
     */
    @GetMapping(value = "/chip/{chip}")
    public String buscarAnimalPorChip(@PathVariable String chip, Model model, RedirectAttributes redirectAttributes) {
        try {
            AnimalGenericoResposta animal = animalService.buscarAnimalPorChip(chip);
            model.addAttribute("animal", animal);
            return "animais/detalhes";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Animal não encontrado com o chip: " + chip);
            return "redirect:/animais";
        }
    }
    
    /**
     * Exibe o formulário para criar novo animal1
     */
    @GetMapping(value = "/form")
    public String formNovoAnimal(Model model) {
        model.addAttribute("animal", new AnimalGenericoRequisicao());
        montarComboStatusEnum(model);
        return "animais/formulario";
    }
    
    /**
     * Processa o formulário de criação de animal
     */
    @PostMapping("/salvar")
    public String salvarNovoAnimal(@Valid @ModelAttribute("animal") AnimalGenericoRequisicao animal, 
                                  BindingResult result, 
                                  RedirectAttributes redirectAttributes,
                                  Model model) {
        if (result.hasErrors()) {
           montarComboStatusEnum(model);
            return "animais/formulario";
        }
        
        // Aqui você precisará implementar o método de cadastro no seu serviço
        // Supondo que você tenha uma versão de serviço que retorne o ID
        AnimalGenericoResposta novoAnimal = animalService.salvarAnimal(animal);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Animal cadastrado com sucesso!");
        return "redirect:/animais/" + novoAnimal.getId();
    }




    /**
     * Processa o formulário de edição de animal
     */
    @PostMapping(value = "/{id}/editar")
    public String atualizarAnimal(@PathVariable Long id,
                                 @Valid @ModelAttribute("animal") AnimalGenericoRequisicao animal,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        if (result.hasErrors()) {
            montarComboStatusEnum(model);
            return "redirect:/animais/" + id;
        }

        System.out.println("id passado do animal" + id);

        animalService.atualizarAnimal(id, animal);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Animal atualizado com sucesso!");
        return "redirect:/animais/" + id;
    }




    /**
     * Exclui um animal
     */
    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id,
                          RedirectAttributes ra,
                          HttpServletRequest req,
                          HttpServletResponse res) {
        // Se der problema, o service lança BaseApplicationError e o Advice cuida.
        animalService.deletarPorId(id);

        if (isHtmx(req)) {
            res.setStatus(204);
            res.setHeader("HX-Trigger",
                    "{\"toast\":{\"type\":\"success\",\"text\":\"Animal excluído com sucesso!\"}}");
            return null; // sem view (HTMX)
        }

        ra.addFlashAttribute("mensagemSucesso", "Animal excluído com sucesso!");
        return "redirect:/animais"; // após excluir, volte para a lista
    }

    private boolean isHtmx(HttpServletRequest req) {
        return req.getHeader("HX-Request") != null;
    }
    
    /**
     * Exibe formulário para registrar óbito
     */
    @GetMapping(value = "/{id}/obito")
    public String formObito(@PathVariable Long id, Model model) {
        AnimalGenericoResposta animal = animalService.buscarAnimalPorId(id);
        model.addAttribute("animalId", id);
        model.addAttribute("chipId", animal.getChipId());
        return "animais/obito";
    }
    
    /**
     * Processa o registro de óbito
     */
    @PostMapping(value = "/{id}/obito")
    public String registrarObito(@PathVariable Long id,
                               @RequestParam String chipId,
                               @RequestParam String motivoObito,
                               @RequestParam java.util.Date dataObito,
                               RedirectAttributes redirectAttributes) {
        try {
            AnimalObituarioResquisicao obito = new AnimalObituarioResquisicao(chipId, motivoObito, dataObito);
            animalService.declararObito(obito);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Óbito registrado com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao registrar óbito: " + e.getMessage());
        }
        return "redirect:/animais";
    }
    
    /**
     * Exibe formulário para adicionar adoção conjunta
     */
    @GetMapping("/conjunto")
    public String formConjunto(Model model) {
        return "animais/conjunto";
    }
    
    /**
     * Processa a adição de adoção conjunta
     */
    @PostMapping("/conjunto")
    public String adicionarAdocaoConjunta(@RequestParam Long principal,
                                         @RequestParam Long animal1,
                                         @RequestParam(required = false) Long animal2,
                                         RedirectAttributes redirectAttributes) {
        try {
            Map<String, Long> ids = new HashMap<>();
            ids.put("principal", principal);
            ids.put("animal1", animal1);
            
            if (animal2 != null) ids.put("animal2", animal2);
            
            animalService.adicionarAdocaoConjuntaEmAnimal(ids);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Adoção conjunta registrada com sucesso!");
            return "redirect:/animais/" + principal;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao registrar adoção conjunta: " + e.getMessage());
            return "redirect:/animais/conjunto";
        }
    }


    public void carregarComboFiltros(Model model,
                                     String nome,
                                     String raca,
                                     EspecieEnum especie,
                                     PorteEnum porte,
                                     StatusEnum status,
                                     String doenca,
                                     String comportamento,
                                     MaturidadeEnum maturidade,
                                     OrigemAnimalEnum origem,
                                     SexoEnum sexo) {

        model.addAttribute("filtroNome", nome);
        model.addAttribute("filtroRaca", raca);
        model.addAttribute("filtroEspecie", especie);
        model.addAttribute("filtroPorte", porte);
        model.addAttribute("filtroStatus", status);
        model.addAttribute("filtroDoenca", doenca);
        model.addAttribute("filtroComportamento", comportamento);
        model.addAttribute("filtroMaturidade", maturidade);
        model.addAttribute("filtroOrigem", origem);
        model.addAttribute("filtroSexo", sexo);
    }

    public void montarComboStatusEnum(Model model) {
        model.addAttribute("especies", EspecieEnum.values());
        model.addAttribute("portes", PorteEnum.values());
        model.addAttribute("status", StatusEnum.values());
        model.addAttribute("maturidades", MaturidadeEnum.values());
        model.addAttribute("origens", OrigemAnimalEnum.values());
        model.addAttribute("sexos", SexoEnum.values());
    }

}
