package br.com.unicsul.catalogo.controller;

import br.com.unicsul.catalogo.domain.Categoria;
import br.com.unicsul.catalogo.service.CategoriaService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    private final CategoriaService categoriaService;

    public HomeController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public String home(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // Obtém todas as categorias ativas ordenadas por nome
        List<Categoria> categorias = categoriaService.listarAtivas();
        
        // Adiciona o nome do usuário ao modelo se estiver autenticado
        if (userDetails != null) {
            model.addAttribute("nomeUsuario", userDetails.getUsername());
        }
        
        model.addAttribute("categorias", categorias);
        model.addAttribute("pageTitle", "Início");
        return "index";
    }

    @GetMapping("/sobre")
    public String sobre(Model model) {
        model.addAttribute("pageTitle", "Sobre");
        return "sobre";
    }
    
    @GetMapping("/acesso-negado")
    public String acessoNegado() {
        return "erros/acesso-negado";
    }
}
