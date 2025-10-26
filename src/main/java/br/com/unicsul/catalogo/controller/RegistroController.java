package br.com.unicsul.catalogo.controller;

import br.com.unicsul.catalogo.domain.Usuario;
import br.com.unicsul.catalogo.dto.UsuarioRegistroDTO;
import br.com.unicsul.catalogo.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/cadastro")
public class RegistroController {

    private final AuthService authService;

    public RegistroController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public String mostrarFormularioRegistro(Model model) {
        if (!model.containsAttribute("usuario")) {
            model.addAttribute("usuario", new UsuarioRegistroDTO());
        }
        model.addAttribute("pageTitle", "Cadastro de Novo Usuário");
        return "auth/registro";
    }

    @PostMapping
    public String registrarUsuario(
            @Valid @ModelAttribute("usuario") UsuarioRegistroDTO usuarioDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.usuario", result);
            redirectAttributes.addFlashAttribute("usuario", usuarioDTO);
            return "redirect:/cadastro";
        }

        try {
            Usuario usuario = authService.registrarNovoUsuario(usuarioDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Cadastro realizado com sucesso! Faça login para continuar.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("usuario", usuarioDTO);
            return "redirect:/cadastro";
        }
    }
}
