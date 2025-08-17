package org.ong.pet.pex.backendpetx.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Habilita @PreAuthorize e @PostAuthorize
public class SecurityConfigurations {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(Customizer.withDefaults())
                .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .maximumSessions(1)
                    .maxSessionsPreventsLogin(false)
                )
                .authorizeHttpRequests(authorize -> authorize
                        // Recursos estáticos públicos
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()

                        // Páginas públicas
                        .requestMatchers("/login", "/register", "/forgot-password").permitAll()

                        // APIs públicas
                        .requestMatchers("/api/auth/**", "/api/usuarios/registrar").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/health/status").permitAll()

                        // Dashboard - ADMIN e COLABORADOR
                        .requestMatchers("/dashboard").hasAnyRole("ADMIN", "COLABORADOR")

                        // Gestão de Usuários - APENAS ADMIN
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/usuarios/**").hasRole("ADMIN")

                        // Gestão de Pets - ADMIN e COLABORADOR
                        .requestMatchers("/pets/**").hasAnyRole("ADMIN", "COLABORADOR")
                        .requestMatchers("/animais/**").hasAnyRole("ADMIN", "COLABORADOR")

                        // Gestão de Tutores - ADMIN e COLABORADOR (substitui Clientes)
                        .requestMatchers("/tutores/**").hasAnyRole("ADMIN", "COLABORADOR")

                        // Consultas - ADMIN e COLABORADOR
                        .requestMatchers("/consultas/**").hasAnyRole("ADMIN", "COLABORADOR")

                        // Relatórios - APENAS ADMIN
                        .requestMatchers("/relatorios/**").hasRole("ADMIN")

                        // Configurações - APENAS ADMIN
                        .requestMatchers("/configuracoes/**").hasRole("ADMIN")

                        .requestMatchers("/boletins/**").hasAnyRole("ADMIN", "COLABORADOR")

                        // Todas as outras páginas precisam de autenticação
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("/dashboard", true)
                    .failureUrl("/login?error=true")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .permitAll()
                )
                .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout=true")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll()
                )
                .exceptionHandling(exceptions -> exceptions
                    .accessDeniedPage("/access-denied")
                );

        return httpSecurity.build();
    }

    /**
     * Hierarquia de roles - ADMIN herda permissões de COLABORADOR
     */
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_ADMIN > ROLE_COLABORADOR");
        return hierarchy;
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
