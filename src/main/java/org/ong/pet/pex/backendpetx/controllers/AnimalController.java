package org.ong.pet.pex.backendpetx.controllers;

import jakarta.validation.Valid;
import org.ong.pet.pex.backendpetx.dto.request.AnimalGenericoRequisicao;
import org.ong.pet.pex.backendpetx.dto.request.AnimalObituarioResquisicao;
import org.ong.pet.pex.backendpetx.dto.response.AnimalGenericoResposta;
import org.ong.pet.pex.backendpetx.dto.response.AnimalPaginadoResposta;
import org.ong.pet.pex.backendpetx.enums.*;
import org.ong.pet.pex.backendpetx.service.AnimalService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
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
    @GetMapping
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
                            @PageableDefault(size = 20) Pageable pageable) {
        Page<AnimalPaginadoResposta> page = animalService.paginarAnimais(
                nome, raca, especie, porte, status, doenca, comportamento, maturidade, origem, sexo, pageable
        );
        model.addAttribute("page", page);
        model.addAttribute("currentPage", "/animais");
        model.addAttribute("animais", page.getContent());
        
        // Adicionar enums ao modelo para uso nos selects dos formulários
        model.addAttribute("especies", EspecieEnum.values());
        model.addAttribute("portes", PorteEnum.values());
        model.addAttribute("statusEnum", StatusEnum.values());
        model.addAttribute("maturidades", MaturidadeEnum.values());
        model.addAttribute("origens", OrigemAnimalEnum.values());
        model.addAttribute("sexos", SexoEnum.values());
        
        // Filtros atuais para manter valores no form
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
        
        return "animais/lista";
    }

    /**
     * Exibe detalhes de um animal específico
     */
    @GetMapping("/{id}")
    public String detalhesAnimal(@PathVariable Long id, Model model) {
        AnimalGenericoResposta animal = animalService.buscarAnimalPorId(id);
        model.addAttribute("animal", animal);
        return "animais/detalhes";
    }
    
    /**
     * Busca animal por chip
     */
    @GetMapping("/chip/{chip}")
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
     * Exibe o formulário para criar novo animal
     */
    @GetMapping("/novo")
    public String formNovoAnimal(Model model) {
        model.addAttribute("animal", new AnimalGenericoRequisicao());
        model.addAttribute("especies", EspecieEnum.values());
        model.addAttribute("portes", PorteEnum.values());
        model.addAttribute("statusEnum", StatusEnum.values());
        model.addAttribute("maturidades", MaturidadeEnum.values());
        model.addAttribute("origens", OrigemAnimalEnum.values());
        model.addAttribute("sexos", SexoEnum.values());
        return "animais/formulario";
    }
    
    /**
     * Processa o formulário de criação de animal
     */
    @PostMapping("/novo")
    public String salvarNovoAnimal(@Valid @ModelAttribute("animal") AnimalGenericoRequisicao animal, 
                                  BindingResult result, 
                                  RedirectAttributes redirectAttributes,
                                  Model model) {
        if (result.hasErrors()) {
            model.addAttribute("especies", EspecieEnum.values());
            model.addAttribute("portes", PorteEnum.values());
            model.addAttribute("statusEnum", StatusEnum.values());
            model.addAttribute("maturidades", MaturidadeEnum.values());
            model.addAttribute("origens", OrigemAnimalEnum.values());
            model.addAttribute("sexos", SexoEnum.values());
            return "animais/formulario";
        }
        
        // Aqui você precisará implementar o método de cadastro no seu serviço
        // Supondo que você tenha uma versão de serviço que retorne o ID
        AnimalGenericoResposta novoAnimal = animalService.atualizarAnimal(null, animal);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Animal cadastrado com sucesso!");
        return "redirect:/animais/" + novoAnimal.getId();
    }
    
    /**
     * Exibe o formulário para editar um animal existente
     */
    @GetMapping("/{id}/editar")
    public String formEditarAnimal(@PathVariable Long id, Model model) {
        AnimalGenericoResposta animal = animalService.buscarAnimalPorId(id);
        model.addAttribute("animal", animal);
        model.addAttribute("especies", EspecieEnum.values());
        model.addAttribute("portes", PorteEnum.values());
        model.addAttribute("statusEnum", StatusEnum.values());
        model.addAttribute("maturidades", MaturidadeEnum.values());
        model.addAttribute("origens", OrigemAnimalEnum.values());
        model.addAttribute("sexos", SexoEnum.values());
        return "animais/editar";
    }
    
    /**
     * Processa o formulário de edição de animal
     */
    @PostMapping("/{id}/editar")
    public String atualizarAnimal(@PathVariable Long id,
                                 @Valid @ModelAttribute("animal") AnimalGenericoRequisicao animal,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        if (result.hasErrors()) {
            model.addAttribute("especies", EspecieEnum.values());
            model.addAttribute("portes", PorteEnum.values());
            model.addAttribute("statusEnum", StatusEnum.values());
            model.addAttribute("maturidades", MaturidadeEnum.values());
            model.addAttribute("origens", OrigemAnimalEnum.values());
            model.addAttribute("sexos", SexoEnum.values());
            return "animais/editar";
        }
        
        animalService.atualizarAnimal(id, animal);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Animal atualizado com sucesso!");
        return "redirect:/animais/" + id;
    }
    
    /**
     * Exclui um animal
     */
    @PostMapping("/{id}/excluir")
    public String excluirAnimal(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            animalService.deletarPorId(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Animal excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir animal: " + e.getMessage());
        }
        return "redirect:/animais";
    }
    
    /**
     * Exibe formulário para registrar óbito
     */
    @GetMapping("/{id}/obito")
    public String formObito(@PathVariable Long id, Model model) {
        AnimalGenericoResposta animal = animalService.buscarAnimalPorId(id);
        model.addAttribute("animalId", id);
        model.addAttribute("chipId", animal.getChipId());
        return "animais/obito";
    }
    
    /**
     * Processa o registro de óbito
     */
    @PostMapping("/{id}/obito")
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
}
