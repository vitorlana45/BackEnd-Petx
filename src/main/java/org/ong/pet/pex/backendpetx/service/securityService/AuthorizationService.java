package org.ong.pet.pex.backendpetx.service.securityService;

import org.ong.pet.pex.backendpetx.repositories.UsuarioRepository;
import org.ong.pet.pex.backendpetx.service.exceptions.UsuarioException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public AuthorizationService(UsuarioRepository userRepository){
        this.usuarioRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(username);
    }
}
