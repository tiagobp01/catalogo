package br.com.unicsul.catalogo.config.security;

import br.com.unicsul.catalogo.domain.Usuario;
import br.com.unicsul.catalogo.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserSession {

    @Autowired
    private HttpSession session;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void setUsuario(Usuario usuario) {
        if (usuario != null) {
            session.setAttribute("usuarioId", usuario.getId().toString());
        } else {
            session.removeAttribute("usuarioId");
        }
    }

    public Usuario getUsuario() {
        Object usuarioId = session.getAttribute("usuarioId");
        if (usuarioId == null) {
            return null;
        }
        try {
            java.util.UUID id = java.util.UUID.fromString(usuarioId.toString());
            return usuarioRepository.findById(id).orElse(null);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public boolean isAdmin() {
        Usuario usuario = getUsuario();
        return usuario != null && usuario.getPerfis().stream()
                .anyMatch(up -> up.getPerfil().getNome().equals("ROLE_ADMIN"));
    }

    public boolean isAutenticado() {
        return getUsuario() != null;
    }

    public void limpar() {
        session.removeAttribute("usuarioId");
    }
}
