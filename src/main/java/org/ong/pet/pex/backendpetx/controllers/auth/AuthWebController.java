package org.ong.pet.pex.backendpetx.controllers.auth;

import org.ong.pet.pex.backendpetx.dto.response.RespostaBuscarUsuarioPadrao;
import org.ong.pet.pex.backendpetx.service.UsuarioService;
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

    public AuthWebController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /** Home: manda pro lugar certo conforme estado de login */
    @GetMapping(value = "/", name = "AUTH#HOME")
    public String home(Authentication auth) {
        return (auth != null && auth.isAuthenticated() && !trust.isAnonymous(auth))
                ? "redirect:/home"
                : "redirect:/login";
    }
    /** Página de login (GET). O POST /login é tratado pelo Spring Security. */
    @GetMapping(value = "/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Authentication auth,
                            Model model) {


        if (auth != null && auth.isAuthenticated() && !trust.isAnonymous(auth)) {
            return "redirect:/home";
        }

        if (error != null)   model.addAttribute("errorMessage",  "Credenciais inválidas!");
        if (logout != null)  model.addAttribute("logoutMessage", "Logout realizado com sucesso!");

        return "auth/login";
    }

    /** Registro — placeholder por enquanto */
    @GetMapping(value = "/register")
    public String registerPage() {
        return "auth/register";
    }

    @PostMapping(value = "/register", name = "AUTH#PROCESSAR_REGISTRO")
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
    @GetMapping(value = "/forgot-password")
    public String forgotPasswordPage() {
        return "auth/forgot-password";
    }

    @PostMapping(value = "/forgot-password", name = "AUTH#PROCESSAR_ESQUECI_SENHA")
    public String processForgotPassword(@RequestParam String email, Model model) {
        // TODO: implementar envio de email/token
        model.addAttribute("successMessage",
                "Se o e-mail existir, você receberá instruções para recuperação.");
        return "auth/forgot-password";
    }

    @GetMapping(value = "/dashboard")
    public String dashboard(Model model, Principal principal) {
        try {
            RespostaBuscarUsuarioPadrao usuario = usuarioService.buscarUsuarioPorEmail(principal.getName());
            model.addAttribute("username", usuario.nome());
            model.addAttribute("currentPage", "/Home");

            System.out.println("entrou aqui!!");


//            SmartPageBuilder.homePage();
//            SmartPageHelper.setupHome(model, totalAnimais, totalTutores, totalConsumo);

            return "index";
        } catch (Exception e) {
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

