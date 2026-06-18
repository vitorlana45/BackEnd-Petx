package org.ong.pet.pex.backendpetx.repositories;

import org.ong.pet.pex.backendpetx.entities.RecuperarSenha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface RecuperarSenhaRepository extends JpaRepository<RecuperarSenha, Long> {

    @Query("SELECT obj FROM RecuperarSenha obj WHERE obj.token = :token AND obj.expiracaoToken > :now")
    List<RecuperarSenha> procurarTokensValidos(String token, Instant now);
}
