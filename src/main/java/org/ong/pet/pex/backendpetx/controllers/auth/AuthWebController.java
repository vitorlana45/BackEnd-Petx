package org.ong.pet.pex.backendpetx.controllers.auth;

import org.ong.pet.pex.backendpetx.dto.response.RespostaBuscarUsuarioPadrao;
import org.ong.pet.pex.backendpetx.service.AnimalService;
import org.ong.pet.pex.backendpetx.service.DashboardService;
import org.ong.pet.pex.backendpetx.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;

import java.security.Principal;

/**
 * Controller web para autenticação - 100% Thymeleaf
 * Gerencia páginas de login, registro e recuperação de senha
 */
@Controller
public class AuthWebController {

    private final AuthenticationTrustResolver trust = new AuthenticationTrustResolverImpl();
    private final UsuarioService usuarioService;

    private final DashboardService dashboardService;

    public AuthWebController(UsuarioService usuarioService, DashboardService dashboardService) {
        this.usuarioService = usuarioService;
        this.dashboardService = dashboardService;

    }

    /** Home: manda pro lugar certo conforme estado de login */
    @GetMapping("/")
    public String home(Authentication auth) {
        return (auth != null && auth.isAuthenticated() && !trust.isAnonymous(auth))
                ? "redirect:/dashboard"
                : "redirect:/login";
    }

    /** Página de login (GET). O POST /login é tratado pelo Spring Security. */
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Authentication auth,
                            Model model) {

        // já logado? manda pra dashboard
        if (auth != null && auth.isAuthenticated() && !trust.isAnonymous(auth)) {
            return "redirect:/dashboard";
        }

        // mensagens padrão do Spring Security (opcional: pode usar ${param.error} direto no HTML)
        if (error != null)   model.addAttribute("errorMessage",  "Credenciais inválidas!");
        if (logout != null)  model.addAttribute("logoutMessage", "Logout realizado com sucesso!");

        return "auth/login";
    }

    /** Registro — placeholder por enquanto */
    @GetMapping("/register")
    public String registerPage() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegister(@RequestParam String nome,
                                  @RequestParam String email,
                                  @RequestParam String senha,
                                  @RequestParam String confirmarSenha,
                                  Model model) {
        if (!senha.equals(confirmarSenha)) {
            model.addAttribute("errorMessage", "Senhas não conferem!");
            return "auth/register";
        }
        // TODO: implementar criação de usuário + encode da senha + role padrão
        model.addAttribute("successMessage", "Usuário registrado com sucesso! Faça login.");
        return "auth/login";
    }

    /** Recuperação de senha — placeholder */
    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email, Model model) {
        // TODO: implementar envio de email/token
        model.addAttribute("successMessage",
                "Se o e-mail existir, você receberá instruções para recuperação.");
        return "auth/forgot-password";
    }

    /** Dashboard (somente autenticado). Usa Principal p/ exibir nome. */
    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        try {
            // Forçar o tipo de conteúdo para HTML
            RespostaBuscarUsuarioPadrao usuario = usuarioService.buscarUsuarioPorEmail(principal.getName());
            model.addAttribute("username", usuario.nome());

            // Adiciona a variável page para substituir o uso de #request.requestURI
            model.addAttribute("currentPage", "/dashboard");

            // Retorna a view do dashboard
            Long totalAnimais = dashboardService.getTotalAnimais();
            Long totalTutores = dashboardService.getTotalTutores();
            Long totalConsumo = dashboardService.totalConsumo();
            model.addAttribute("totalAnimais", totalAnimais);
            model.addAttribute("totalTutores", totalTutores);
            model.addAttribute("totalConsumo", totalConsumo);

            // Adiciona notificação para garantir que o alerta seja exibido
            model.addAttribute("notification", true);

            return "dashboard/index";
        } catch (Exception e) {
            // Log do erro
            e.printStackTrace();
            model.addAttribute("error", "Ocorreu um erro ao carregar o dashboard: " + e.getMessage());
            return "error/generic";
        }
    }

    /** Página para 403 */
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "error/access-denied";
    }
}

