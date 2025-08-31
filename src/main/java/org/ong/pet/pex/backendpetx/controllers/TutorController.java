package org.ong.pet.pex.backendpetx.controllers;

import jakarta.validation.Valid;
import org.ong.pet.pex.backendpetx.dto.request.AtualizarTutorRequisicao;
import org.ong.pet.pex.backendpetx.dto.request.CadastrarTutorRequisicao;
import org.ong.pet.pex.backendpetx.dto.response.AnimalPaginadoResposta;
import org.ong.pet.pex.backendpetx.dto.response.TutorDTOResposta;
import org.ong.pet.pex.backendpetx.enums.*;
import org.ong.pet.pex.backendpetx.service.AnimalService;
import org.ong.pet.pex.backendpetx.service.TutorService;
import org.ong.pet.pex.backendpetx.service.impl.AnimalServiceImpl;
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
import java.util.List;

@Controller
@RequestMapping("/tutores")
@PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
public class TutorController {

    private final TutorService tutorService;
    private final AnimalService animalService;

    public TutorController(TutorService tutorService, AnimalService animalService) {
        this.tutorService = tutorService;
        this.animalService = animalService;
    }

    /**
     * Lista tutores com filtros e paginação
     */
    @GetMapping()
    public String listarTutores(Model model,
                         @RequestParam(required = false) String nome,
                         @RequestParam(required = false) String cep,
                         @RequestParam(required = false) String cidade,
                         @RequestParam(required = false) String estado,
                         @RequestParam(required = false) Integer idade,
                         @PageableDefault(size = 10) Pageable pageable) {
        Page<TutorDTOResposta> page = tutorService.findAllTutorPaginacao(nome, cep, cidade, estado, idade, pageable);
        model.addAttribute("page", page);
        model.addAttribute("currentPage", "/tutores");
        model.addAttribute("tutores", page.getContent());
        model.addAttribute("filtroNome", nome);
        model.addAttribute("filtroCep", cep);
        model.addAttribute("filtroCidade", cidade);
        model.addAttribute("filtroEstado", estado);
        model.addAttribute("filtroIdade", idade);
        return "tutores/lista";
    }

    /**
     * Exibe formulário para novo tutor
     */
    @GetMapping(value = "/form")
    public String formNovoTutor(Model model) {

        model.addAttribute("buscaAnimaisUrl", "/animais/selector");

        montarComboFiltroAnimal(model);

        var animalChips = new ArrayList<String>();

        var tutor = new CadastrarTutorRequisicao("","","", 0,"","","","", "", animalChips);

        model.addAttribute("tutor",tutor);


        return "tutores/formulario";
    }


    /**
     * Processa o formulário de criação de tutor
     */
    @PostMapping(value = "/salvar")
    public String cadastrarTutor(@Valid @ModelAttribute CadastrarTutorRequisicao tutor,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("tutor", tutor);
            montarComboFiltroAnimal(model);
            return "tutores/formulario";
        }

        try {
            tutorService.cadastrarTutor(tutor);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Tutor cadastrado com sucesso!");
            return "redirect:/tutores";
        } catch (Exception e) {
            model.addAttribute("tutor", tutor);
            montarComboFiltroAnimal(model);
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao cadastrar tutor: " + e.getMessage());
            return "tutores/formulario";
        }
    }

    @GetMapping("/selector")
    public String selector(@RequestParam(required = false) String nome,
                           @RequestParam(required = false) String raca,
                           @RequestParam(required = false) EspecieEnum especie,
                           @RequestParam(required = false) PorteEnum porte,
                           @RequestParam(required = false) SaudeEnum saude,
                           @RequestParam(required = false) String comportamento,
                           @RequestParam(required = false) MaturidadeEnum maturidade,
                           @RequestParam(required = false) OrigemAnimalEnum origem,
                           @RequestParam(required = false) SexoEnum sexo,
                           @PageableDefault(size = 8) Pageable pageable,
                           Model model) {

        var page = this.animalService.paginarAnimaisParaAdocao(nome, raca, especie, porte, saude, comportamento, maturidade, origem, sexo, pageable);

        model.addAttribute("page", page);
        model.addAttribute("currentPage", "/tutores");
        model.addAttribute("animais", page.getContent());
        montarComboStatusEnum(model);

        return "animais/selector :: selector";

    }


    public void montarComboStatusEnum(Model model) {
        model.addAttribute("especies", EspecieEnum.values());
        model.addAttribute("portes", PorteEnum.values());
        model.addAttribute("status", SaudeEnum.values());
        model.addAttribute("maturidades", MaturidadeEnum.values());
        model.addAttribute("origens", OrigemAnimalEnum.values());
        model.addAttribute("sexos", SexoEnum.values());
    }

    /**
     * Exibe detalhes de um tutor
     */
    @GetMapping(value = "/{cpf}")
    public String detalhesTutor(@PathVariable String cpf, Model model, RedirectAttributes redirectAttributes) {
        if (cpf == null || cpf.isBlank() || cpf.equalsIgnoreCase("salvar")) {
            redirectAttributes.addFlashAttribute("mensagemErro", "CPF inválido ou não encontrado.");
            return "redirect:/tutores";
        }
        TutorDTOResposta tutor = tutorService.buscarTutorPorCpf(cpf);
        model.addAttribute("tutor", tutor);
        return "tutores/detalhes";
    }

    /**
     * Exibe formulário para editar tutor
     */
    @GetMapping(value = "/{cpf}/editar")
    public String formEditarTutor(@PathVariable String cpf, Model model) {
        TutorDTOResposta tutor = tutorService.buscarTutorPorCpf(cpf);
        model.addAttribute("tutor", tutor);
        model.addAttribute("cpf", cpf);
        return "tutores/editar";
    }

    /**
     * Processa o formulário de edição de tutor
     */
    @PostMapping(value = "/{cpf}/editar")
    public String atualizarTutor(@PathVariable String cpf,
                               @Valid @ModelAttribute AtualizarTutorRequisicao tutor,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "tutores/editar";
        }

        try {
            String novoCpf = tutorService.atualizarDadosTutor(cpf, tutor);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Tutor atualizado com sucesso!");
            return "redirect:/tutores/" + novoCpf;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao atualizar tutor: " + e.getMessage());
            return "tutores/editar";
        }
    }

    /**
     * Exclui um tutor
     */
    @PostMapping(value = "/{cpf}/excluir")
    public String excluirTutor(@PathVariable String cpf, RedirectAttributes redirectAttributes) {
        try {
            tutorService.deletarTutorPorCpf(cpf);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Tutor excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir tutor: " + e.getMessage());
        }
        return "redirect:/tutores";
    }

    private void montarComboFiltroAnimal(Model model){
        model.addAttribute("filtroNome", "");
        model.addAttribute("portes", PorteEnum.values());
        model.addAttribute("especies", EspecieEnum.values());
        model.addAttribute("sexos", SexoEnum.values());
        model.addAttribute("status", List.of(SaudeEnum.SAUDAVEL, SaudeEnum.DOENTE));
        model.addAttribute("origens", OrigemAnimalEnum.values());
    }

}
