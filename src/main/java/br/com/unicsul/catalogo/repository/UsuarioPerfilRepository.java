package br.com.unicsul.catalogo.repository;

import br.com.unicsul.catalogo.domain.Perfil;
import br.com.unicsul.catalogo.domain.Usuario;
import br.com.unicsul.catalogo.domain.UsuarioPerfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioPerfilRepository extends JpaRepository<UsuarioPerfil, Long> {
    
    Optional<UsuarioPerfil> findByUsuarioAndPerfil(Usuario usuario, Perfil perfil);
    
    List<UsuarioPerfil> findByUsuario(Usuario usuario);
    
    List<UsuarioPerfil> findByUsuarioId(UUID usuarioId);
    
    boolean existsByUsuarioAndPerfil(Usuario usuario, Perfil perfil);
    
    void deleteByUsuarioAndPerfil(Usuario usuario, Perfil perfil);
}
