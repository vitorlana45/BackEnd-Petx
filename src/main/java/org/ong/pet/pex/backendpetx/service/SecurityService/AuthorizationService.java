package org.ong.pet.pex.backendpetx.service.SecurityService;

import org.ong.pet.pex.backendpetx.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    private UsuarioRepository usuarioRepository;

    @Autowired
    public AuthorizationService(UsuarioRepository userRepository){
        this.usuarioRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(username);
    }
}
