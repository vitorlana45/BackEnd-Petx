package org.ong.pet.pex.backendpetx.controller.admin;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller para funcionalidades administrativas - APENAS ADMIN
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @GetMapping(name = "ADMIN#DASHBOARD")
    public String adminDashboard(Model model) {
        model.addAttribute("totalUsuarios", 15);
        model.addAttribute("usuariosAtivos", 12);
        model.addAttribute("usuariosInativos", 3);
        model.addAttribute("currentPage", "/admin");
        return "admin/dashboard";
    }

    @GetMapping(value = "/usuarios", name = "ADMIN#USUARIOS_LISTAR")
    public String listarUsuarios(Model model) {
        // Aqui você buscaria os usuários do banco
        model.addAttribute("currentPage", "/admin/usuarios");
        return "admin/usuarios/lista";
    }

    @GetMapping(value = "/usuarios/novo", name = "ADMIN#USUARIOS_NOVO")
    public String novoUsuario() {
        return "admin/usuarios/formulario";
    }

    @PostMapping(value = "/usuarios/salvar", name = "ADMIN#USUARIOS_SALVAR")
    public String salvarUsuario(@RequestParam String nome,
                               @RequestParam String email,
                               @RequestParam String senha,
                               @RequestParam String role,
                               RedirectAttributes redirectAttributes) {
        try {
            // Lógica para criar usuário
            redirectAttributes.addFlashAttribute("successMessage", "Usuário criado com sucesso!");
            return "redirect:/admin/usuarios";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao criar usuário: " + e.getMessage());
            return "redirect:/admin/usuarios/novo";
        }
    }

    @GetMapping(value = "/relatorios", name = "ADMIN#RELATORIOS")
    public String relatorios(Model model) {
        return "admin/relatorios/index";
    }

    @GetMapping(value = "/configuracoes", name = "ADMIN#CONFIGURACOES")
    public String configuracoes(Model model) {
        return "admin/configuracoes/index";
    }
}
