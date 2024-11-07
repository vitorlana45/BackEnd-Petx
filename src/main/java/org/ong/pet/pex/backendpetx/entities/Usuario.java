package org.ong.pet.pex.backendpetx.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "usuario_db")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String senha;
    private UserRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role.equals(UserRole.ADMIN)) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else if (this.role.equals(UserRole.USER)) {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            return List.of(); // Sem permissões para outras roles
        }
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}