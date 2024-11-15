package org.ong.pet.pex.backendpetx.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    private final SecurityFilter securityFilter;

    @Autowired
    public SecurityConfigurations(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/api/usuarios/inserir").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/auth/recuperar-token").permitAll()
                        // uso ENDPOINT DOS ADMINISTRADORES DA ONG
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/buscarId/{id}").hasAnyRole("COLABORADOR", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/buscar/{email}").hasAnyRole("COLABORADOR", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/buscar/todos").hasAnyRole("COLABORADOR", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/usuarios/deletar/{id}").hasAnyRole("COLABORADOR", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/animais/cadastrar").hasAnyRole("ADMIN", "COLABORADOR")
                        .requestMatchers(HttpMethod.GET, "/api/animais/buscar/chip/{chip}").hasAnyRole("ADMIN", "COLABORADOR")
                        // uso ENDPOINT DOS COLABORADOR ABAIXO
                        .requestMatchers(HttpMethod.GET, "/api/animais/buscarId/{id}").hasAnyRole("COLABORADOR", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/animais/adicionar/conjuncao/**").hasAnyRole("COLABORADOR", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/animais/cadastrar").hasAnyRole("COLABORADOR", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/animais/atualizar/**").hasAnyRole("COLABORADOR", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/animais/deletar/{id}").hasAnyRole("COLABORADOR", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/animais/adicionar/conjunto/**").hasAnyRole("ADMIN", "COLABORADOR")
                        .requestMatchers(HttpMethod.GET, "/api/animais/listar/**").hasAnyRole("ADMIN", "COLABORADOR")
                        .requestMatchers(HttpMethod.POST, "/api/animais/obito").hasAnyRole("ADMIN", "COLABORADOR")
                        .requestMatchers(HttpMethod.POST).hasAnyRole("ADMIN", "COLABORADOR")
                        .requestMatchers(HttpMethod.GET).hasAnyRole("/api/tutor/todos","ADMIN", "COLABORADOR")
                        .requestMatchers(HttpMethod.GET).hasAnyRole("/api/tutor/{cpf}","ADMIN", "COLABORADOR")
                        .requestMatchers(HttpMethod.PUT).hasAnyRole("/api/tutor/**","ADMIN", "COLABORADOR")
                        .requestMatchers(HttpMethod.DELETE).hasAnyRole("/api/tutor/{cpf}","ADMIN", "COLABORADOR")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}