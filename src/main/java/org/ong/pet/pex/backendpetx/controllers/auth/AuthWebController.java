package org.ong.pet.pex.backendpetx.controllers.auth;

import org.ong.pet.pex.backendpetx.dto.response.RespostaBuscarUsuarioPadrao;
import org.ong.pet.pex.backendpetx.service.DashboardService;
import org.ong.pet.pex.backendpetx.service.UsuarioService;
import org.ong.pet.pex.backendpetx.controllers.helper.SmartPageHelper;
//import org.petx.dto.SmartPageBuilder;
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
                ? "index"
                : "redirect:/login";
    }

    /** Página de login (GET). O POST /login é tratado pelo Spring Security. */
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Authentication auth,
                            Model model) {


        if (auth != null && auth.isAuthenticated() && !trust.isAnonymous(auth)) {
            return "index";
        }


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

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        try {
            RespostaBuscarUsuarioPadrao usuario = usuarioService.buscarUsuarioPorEmail(principal.getName());
            model.addAttribute("username", usuario.nome());
            model.addAttribute("currentPage", "/Home");


            Long totalAnimais = dashboardService.getTotalAnimais();
            Long totalTutores = dashboardService.getTotalTutores();
            Long totalConsumo = dashboardService.totalConsumo();

//            SmartPageBuilder.homePage();
            SmartPageHelper.setupHome(model, totalAnimais, totalTutores, totalConsumo);

            model.addAttribute("animaisPorStatus", obterAnimaisPorStatusMock());
            model.addAttribute("crescimentoMensal", obterCrescimentoMensalMock());
            model.addAttribute("notification", true);

            return "index";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Ocorreu um erro ao carregar o dashboard: " + e.getMessage());
            return "error/generic";
        }
    }

    /**
     * Mock para animais por status até implementarmos corretamente
     */
    private java.util.Map<String, Long> obterAnimaisPorStatusMock() {
        java.util.Map<String, Long> statusMap = new java.util.HashMap<>();
        statusMap.put("SAUDAVEL", 25L);
        statusMap.put("DOENTE", 8L);
        statusMap.put("ADOTADO", 45L);
        statusMap.put("FALECIDO", 3L);
        return statusMap;
    }

    /**
     * Mock para crescimento mensal até implementarmos corretamente
     */
    private java.util.Map<String, Object> obterCrescimentoMensalMock() {
        java.util.Map<String, Object> crescimento = new java.util.HashMap<>();
        crescimento.put("novosAnimaisMes", 12L);
        crescimento.put("novosTutoresMes", 8L);
        crescimento.put("adocoesMes", 15L);
        crescimento.put("boletinsMes", 5L);
        return crescimento;
    }

    /** Página para 403 */
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "error/access-denied";
    }
}

