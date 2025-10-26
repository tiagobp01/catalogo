package br.com.unicsul.catalogo.repository;

import br.com.unicsul.catalogo.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    
    Optional<Usuario> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    Page<Usuario> findByAtivoTrue(Pageable pageable);
    
    Page<Usuario> findByAtivoFalse(Pageable pageable);
    
    @Query("SELECT u FROM Usuario u WHERE " +
           "LOWER(u.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :termo, '%'))")
    Page<Usuario> search(@Param("termo") String termo, Pageable pageable);
    
    @Query("SELECT DISTINCT u FROM Usuario u LEFT JOIN FETCH u.perfis p LEFT JOIN FETCH p.perfil ORDER BY u.nome")
    List<Usuario> findAllWithPerfis();
}
