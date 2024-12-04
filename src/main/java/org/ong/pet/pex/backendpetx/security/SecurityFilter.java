package org.ong.pet.pex.backendpetx.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.ong.pet.pex.backendpetx.entities.Usuario;
import org.ong.pet.pex.backendpetx.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {


    private final TokenService tokenService;

    private final UsuarioRepository userRepository;

    @Autowired
    public SecurityFilter(TokenService tokenService, UsuarioRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var token = this.recuperarToken(request);
        if (token != null) {
            var email = tokenService.validarToken(token);
            Usuario user = userRepository.findUsuarioByEmail(email);

            if (user == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"message\": \"Usuário não encontrado, verifique suas credenciais!\"}");
                return;
            }

            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else if (!verificarRotasLivres(request.getRequestURI())) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"Token não informado!\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }


    private String recuperarToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        return authHeader.replace("Bearer ", "");
    }

    private boolean verificarRotasLivres(String uri) {
        final String LOGIN_URL = "/api/auth/login";
        final String RECOVER_URL = "/api/auth/recuperar-token";
        final String REGISTRAR_USER_URL = "/api/usuarios/registrar";
        final String NOVA_SENHA_URL = "/api/auth/nova-senha";

        return uri.startsWith(LOGIN_URL) ||
               uri.startsWith(RECOVER_URL) ||
               uri.startsWith(REGISTRAR_USER_URL) ||
               uri.startsWith(NOVA_SENHA_URL);

    }


}