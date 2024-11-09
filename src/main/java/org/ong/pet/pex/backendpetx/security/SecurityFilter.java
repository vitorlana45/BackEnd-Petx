package org.ong.pet.pex.backendpetx.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.ong.pet.pex.backendpetx.entities.Usuario;
import org.ong.pet.pex.backendpetx.repositories.UsuarioRepository;
import org.ong.pet.pex.backendpetx.service.exceptions.UsuarioNaoEncontrado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }


    private String recuperarToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }

}