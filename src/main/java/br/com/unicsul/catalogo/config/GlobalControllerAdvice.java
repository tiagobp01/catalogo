package br.com.unicsul.catalogo.config;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/admin")) {
            model.addAttribute("activePage", "admin");
        }
        // Adicione mais condições conforme necessário para outras páginas
    }
}