package br.com.unicsul.catalogo.config.security;

import br.com.unicsul.catalogo.domain.Usuario;
import br.com.unicsul.catalogo.repository.UsuarioRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.Logger;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    // Using request.getSession() instead of injected HttpSession to avoid thread-safety issues

    private static final Logger logger = Logger.getLogger(CustomAuthenticationSuccessHandler.class.getName());

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                      HttpServletResponse response, 
                                      Authentication authentication) throws IOException, ServletException {
        
        logger.info("\n=== CustomAuthenticationSuccessHandler ===");
        logger.info("Usuário autenticado: " + authentication.getName());
        logger.info("Autorizações: " + authentication.getAuthorities());
        
        String email = authentication.getName();
        logger.info("Buscando usuário no banco de dados para o email: " + email);
        
        try {
            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado após autenticação bem-sucedida: " + email));
                    
            logger.info("Usuário encontrado: " + usuario.getNome() + " (ID: " + usuario.getId() + ")");
            
            // Armazena apenas o ID do usuário na sessão
            request.getSession().setAttribute("usuarioId", usuario.getId());
            
            // Verifica se o usuário tem papel de administrador
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
                    
            logger.info("Usuário " + email + " tem papel de ADMIN? " + isAdmin);
            
            // Define a URL de redirecionamento padrão
            String redirectUrl = "/";
            
            // Se for admin, redireciona para /admin, caso contrário, para a página inicial
            if (isAdmin) {
                redirectUrl = "/admin";
                logger.info("Redirecionando para a área administrativa");
            } else {
                logger.info("Redirecionando para a página inicial");
            }
            
            // Limpa a URL de redirecionamento salva para evitar redirecionamentos indesejados
            request.getSession().removeAttribute("SPRING_SECURITY_SAVED_REQUEST");
            
            logger.info("Redirecionando para: " + redirectUrl);
            
            // Redireciona para a URL apropriada
            response.sendRedirect(redirectUrl);
            
        } catch (Exception e) {
            logger.severe("Erro durante o processamento pós-autenticação: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("/login?error");
        }
    }
}
