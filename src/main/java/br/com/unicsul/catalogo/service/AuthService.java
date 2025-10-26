package br.com.unicsul.catalogo.service;

import br.com.unicsul.catalogo.domain.Usuario;
import br.com.unicsul.catalogo.dto.UsuarioRegistroDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {
    Usuario registrarNovoUsuario(UsuarioRegistroDTO registroDTO);
    boolean emailExiste(String email);
}
