package org.ong.pet.pex.backendpetx.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.ong.pet.pex.backendpetx.controller.bean.ActionButtonDTO;
import org.ong.pet.pex.backendpetx.controller.helper.SmartPageHelper;
import org.ong.pet.pex.backendpetx.dto.request.AnimalGenericoRequisicao;
import org.ong.pet.pex.backendpetx.dto.request.AtualizarTutorRequisicao;
import org.ong.pet.pex.backendpetx.dto.request.CadastrarTutorRequisicao;
import org.ong.pet.pex.backendpetx.dto.response.TutorResposta;
import org.ong.pet.pex.backendpetx.enums.*;
import org.ong.pet.pex.backendpetx.service.AnimalService;
import org.ong.pet.pex.backendpetx.service.TutorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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


    private static List<ActionButtonDTO> getActionButtonDTOS(){
        List<ActionButtonDTO> actionButtons = new ArrayList<>();
        actionButtons.add(new ActionButtonDTO(
                "Novo Tutor",
                "/tutores/form",
                "fas fa-user-plus me-2",
                "btn btn-primary"));
        return actionButtons;
    }


    @GetMapping
    public String listarTutores(Model model,
                                @RequestParam(required = false) String nome,
                                @RequestParam(required = false) String cep,
                                @RequestParam(required = false) String cidade,
                                @RequestParam(required = false) String estado,
                                @RequestParam(required = false) Integer idade,
                                @RequestHeader(value = "HX-Request", defaultValue = "false") boolean htmx,
                                @PageableDefault(size = 10) Pageable pageable,
                                HttpServletResponse resp) {

        SmartPageHelper.setupTutorsPage(model);
        List<ActionButtonDTO> actionButtons = getActionButtonDTOS();
        model.addAttribute("actionButtons", actionButtons);
        // 🔧 Normalização
        nome   = StringUtils.hasText(nome) ? nome : null;
        cep    = StringUtils.hasText(cep) ? cep : null;
        cidade = StringUtils.hasText(cidade) ? cidade : null;
        estado = StringUtils.hasText(estado) ? estado : null;
        // idade: se não veio, já é null (ok)

        model.addAttribute("campos", "clientes/campos :: campos"); // aponta para templates/clientes/campos.html, fragmento "campos"


        Page<TutorResposta> page = tutorService.findAllTutorPaginacao(
                nome, cep, cidade, estado, idade, pageable);

        model.addAttribute("page", page);
        model.addAttribute("tutores", page.getContent());
        model.addAttribute("fNome", nome);
        model.addAttribute("fCep", cep);
        model.addAttribute("fCidade", cidade);
        model.addAttribute("fEstado", estado);
        model.addAttribute("fIdade", idade);



        if (htmx) {
            // Se usar HTMX, devolva só o fragmento da lista
            return "tutores/lista :: lista";
        }
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
    @GetMapping("/{cpf}")
    public String detalhesTutor(@PathVariable String cpf, Model model, RedirectAttributes redirectAttributes) {
        if (cpf == null || cpf.isBlank() || cpf.equalsIgnoreCase("salvar")) {
            redirectAttributes.addFlashAttribute("mensagemErro", "CPF inválido ou não encontrado.");
            return "redirect:/tutores";
        }

        // DTO para exibição
        TutorResposta dto = tutorService.buscarTutorPorCpf(cpf);
        model.addAttribute("tutor", dto);

        // Preenche o form com os dados atuais (mapeie conforme seu DTO/Requisicao)

        List<String> arraysId = new ArrayList<>();

        dto.listaDeAnimais().forEach(an -> arraysId.add(String.valueOf(an.getId())));

        AtualizarTutorRequisicao form = new AtualizarTutorRequisicao(
                dto.cpf(),
                dto.nome(),
                dto.cep(),
                dto.idade(),
                dto.telefone(),
                dto.cidade(),
                dto.bairro(),
                dto.estado(),
                dto.rua(),
                arraysId
        );

        model.addAttribute("tutorForm", form);

        return "tutores/detalhes";
    }

    /**
     * Exibe formulário para editar tutor
     */
    @GetMapping(value = "/{cpf}/editar")
    public String formEditarTutor(@PathVariable String cpf, Model model) {
        TutorResposta tutor = tutorService.buscarTutorPorCpf(cpf);
        model.addAttribute("tutor", tutor);
        model.addAttribute("cpf", cpf);
        return "tutores/editar";
    }
    /**
     * Processa o formulário de edição de tutor
     */
    @PostMapping("/{cpf}/editar")
    public String atualizarTutor(@PathVariable String cpf,
                                 @Valid @ModelAttribute("tutorForm") AtualizarTutorRequisicao tutorForm,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes ra) {

        if (result.hasErrors()) {
            // Garante que o mesmo objeto esteja no model para o BindingResult
            model.addAttribute("tutorForm", tutorForm);

            // Recarrega DTO de exibição
            TutorResposta dto = tutorService.buscarTutorPorCpf(cpf);
            model.addAttribute("tutor", dto);

            // Reabre modal na renderização completa
            model.addAttribute("openEditModal", true);
            return "tutores/detalhes";
        }

        try {
            String novoCpf = tutorService.atualizarDadosTutor(cpf, tutorForm);
            ra.addFlashAttribute("mensagemSucesso", "Tutor atualizado com sucesso!");
            return "redirect:/tutores/" + novoCpf;

        } catch (Exception e) {
            result.reject(null, "Erro ao atualizar tutor: " + e.getMessage());

            // Mantém o form com os erros + reabre modal
            model.addAttribute("tutorForm", tutorForm);
            model.addAttribute("openEditModal", true);

            TutorResposta dto = tutorService.buscarTutorPorCpf(cpf);
            model.addAttribute("tutor", dto);

            return "tutores/detalhes";
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
