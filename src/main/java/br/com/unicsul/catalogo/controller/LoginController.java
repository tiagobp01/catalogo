package br.com.unicsul.catalogo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public String login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model
    ) {
        if (error != null) {
            model.addAttribute("errorMessage", "E-mail ou senha inválidos. Por favor, tente novamente.");
        }
        
        if (logout != null) {
            model.addAttribute("successMessage", "Você saiu com sucesso!");
        }
        
        model.addAttribute("pageTitle", "Login");
        return "auth/login";
    }
}
