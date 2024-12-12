package org.ong.pet.pex.backendpetx.repositories;

import org.ong.pet.pex.backendpetx.entities.Animal;
import org.ong.pet.pex.backendpetx.entities.Tutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, Long> {

    @Modifying
    @Query("delete from Tutor t where :animal member of t.animais")
    void removeAnimalFromTutor(Animal animal);

    Optional<Tutor> findTutorByCpf(String cpf);

    @Query(value = """
            SELECT * FROM tutor_tb t
            WHERE (:nome IS NULL OR LOWER(t.nome) LIKE LOWER(CONCAT('%', :nome, '%')))
            AND (:cep IS NULL OR t.cep = :cep)
            AND (:cidade IS NULL OR LOWER(t.cidade) LIKE LOWER(CONCAT('%', :cidade, '%')))
            AND (:estado IS NULL OR LOWER(t.estado) LIKE LOWER(CONCAT('%', :estado, '%')))
            AND (:idade IS NULL OR t.idade = :idade)
            """,
            countQuery = """
                    SELECT COUNT(*) FROM tutor_tb t
                    WHERE (:nome IS NULL OR LOWER(t.nome) LIKE LOWER(CONCAT('%', :nome, '%')))
                    AND (:cep IS NULL OR t.cep = :cep)
                    AND (:cidade IS NULL OR LOWER(t.cidade) LIKE LOWER(CONCAT('%', :cidade, '%')))
                    AND (:estado IS NULL OR LOWER(t.estado) LIKE LOWER(CONCAT('%', :estado, '%')))
                    AND (:idade IS NULL OR t.idade = :idade)
                    """,
            nativeQuery = true)
    Page<Tutor> findAllTutorPorFiltro(
            @Param("nome") String nome,
            @Param("cep") String cep,
            @Param("cidade") String cidade,
            @Param("estado") String estado,
            @Param("idade") Integer idade,
            Pageable pageable);

}
