package org.ong.pet.pex.backendpetx.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "usuario_tb")
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(unique = true, nullable = false, name = "email")
    private String email;

    @Column(nullable = false, name = "senha")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

    // pensando em rmeover isto
//    @ManyToMany
//    @JoinTable(
//            name = "usuario_animal_tb",
//            joinColumns = @JoinColumn(name = "usuario_id"),
//            inverseJoinColumns = @JoinColumn(name = "animal_id")
//    )
//    private List<Animal> animaisAdotados;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role.equals(UserRole.ADMIN)) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        }
        if (this.role == null || this.role.getRole().isEmpty() || this.role.getRole().isBlank()) {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return List.of();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id) && Objects.equals(email, usuario.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
