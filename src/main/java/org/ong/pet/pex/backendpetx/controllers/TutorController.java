package org.ong.pet.pex.backendpetx.controllers;

import jakarta.validation.Valid;
import org.ong.pet.pex.backendpetx.dto.request.AtualizarTutorRequisicao;
import org.ong.pet.pex.backendpetx.dto.request.CadastrarTutorRequisicao;
import org.ong.pet.pex.backendpetx.dto.response.TutorDTOResposta;
import org.ong.pet.pex.backendpetx.service.TutorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tutores")
@PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
public class TutorController {

    private final TutorService tutorService;

    public TutorController(TutorService tutorService) {
        this.tutorService = tutorService;
    }

    /**
     * Lista tutores com filtros e paginação
     */
    @GetMapping
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
    @GetMapping("/novo")
    public String formNovoTutor(Model model) {
        // Como CadastrarTutorRequisicao é um record, não podemos usar o construtor vazio
        // Vamos passar atributos individuais para o form em vez disso
        model.addAttribute("cpf", "");
        model.addAttribute("nome", "");
        model.addAttribute("cep", "");
        model.addAttribute("idade", "");
        model.addAttribute("telefone", "");
        model.addAttribute("cidade", "");
        model.addAttribute("estado", "");
        model.addAttribute("complemento", "");
        model.addAttribute("bairro", "");
        model.addAttribute("logradouro", "");
        return "tutores/formulario";
    }

    /**
     * Processa o formulário de criação de tutor
     */
    @PostMapping("/novo")
    public String cadastrarTutor(@Valid @ModelAttribute CadastrarTutorRequisicao tutor,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "tutores/formulario";
        }

        try {
            Long tutorId = tutorService.cadastrarTutor(tutor);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Tutor cadastrado com sucesso!");
            return "redirect:/tutores";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao cadastrar tutor: " + e.getMessage());
            return "tutores/formulario";
        }
    }

    /**
     * Exibe detalhes de um tutor
     */
    @GetMapping("/{cpf}")
    public String detalhesTutor(@PathVariable String cpf, Model model) {
        TutorDTOResposta tutor = tutorService.buscarTutorPorCpf(cpf);
        model.addAttribute("tutor", tutor);
        return "tutores/detalhes";
    }

    /**
     * Exibe formulário para editar tutor
     */
    @GetMapping("/{cpf}/editar")
    public String formEditarTutor(@PathVariable String cpf, Model model) {
        TutorDTOResposta tutor = tutorService.buscarTutorPorCpf(cpf);
        model.addAttribute("tutor", tutor);
        model.addAttribute("cpf", cpf);
        return "tutores/editar";
    }

    /**
     * Processa o formulário de edição de tutor
     */
    @PostMapping("/{cpf}/editar")
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
    @PostMapping("/{cpf}/excluir")
    public String excluirTutor(@PathVariable String cpf, RedirectAttributes redirectAttributes) {
        try {
            tutorService.deletarTutorPorCpf(cpf);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Tutor excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir tutor: " + e.getMessage());
        }
        return "redirect:/tutores";
    }
}












