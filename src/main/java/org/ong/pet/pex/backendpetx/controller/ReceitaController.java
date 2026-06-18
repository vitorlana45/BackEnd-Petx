package org.ong.pet.pex.backendpetx.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ong.pet.pex.backendpetx.entity.Receita;
import org.ong.pet.pex.backendpetx.service.ReceitaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
@RequestMapping("/financeiro/receitas")
@RequiredArgsConstructor
public class ReceitaController {

    private final ReceitaService receitaService;

    // --- Endpoints para API REST (se necessário separar ou manter híbirdo) ---
    // Mantendo padrão MVC com Thymeleaf no mesmo controller para simplificar,
    // mas se tiver API separada, mover para pacote api.

    @PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
    @GetMapping
    public String listarReceitas(
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) BigDecimal valor,
            @RequestParam(required = false) LocalDate dataRecebimento,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String formaPagamento,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        // PageControl poderia ser usado aqui se disponível
        Pageable pageable = Pageable.ofSize(size).withPage(page);

        Page<Receita> receitas = receitaService.listarReceitas(descricao, valor, dataRecebimento, categoria, formaPagamento, pageable);

        model.addAttribute("receitas", receitas);
        // Adicionar atributos de filtro para manter no formulário
        model.addAttribute("filtroDescricao", descricao);

        return "financeiro/receitas/lista";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
    @GetMapping("/nova")
    public String novaReceita(Model model) {
        model.addAttribute("receita", new Receita());
        return "financeiro/receitas/form";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
    @PostMapping("/salvar")
    public String salvarReceita(@Valid @ModelAttribute Receita receita, RedirectAttributes redirectAttributes) {
        try {
            if (receita.getId() != null) {
                receitaService.atualizarReceita(receita.getId(), receita);
                redirectAttributes.addFlashAttribute("sucesso", "Receita atualizada com sucesso!");
            } else {
                receitaService.criarReceita(receita);
                redirectAttributes.addFlashAttribute("sucesso", "Receita criada com sucesso!");
            }
            return "redirect:/financeiro/receitas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao salvar receita: " + e.getMessage());
            return "redirect:/financeiro/receitas/nova";
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'COLABORADOR')")
    @GetMapping("/{id}/editar")
    public String editarReceita(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return receitaService.buscarPorId(id)
                .map(receita -> {
                    model.addAttribute("receita", receita);
                    return "financeiro/receitas/form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("erro", "Receita não encontrada.");
                    return "redirect:/financeiro/receitas";
                });
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/{id}/excluir")
    public String excluirReceita(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            receitaService.deletarReceita(id);
            redirectAttributes.addFlashAttribute("sucesso", "Receita excluída.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao excluir: " + e.getMessage());
        }
        return "redirect:/financeiro/receitas";
    }
}

