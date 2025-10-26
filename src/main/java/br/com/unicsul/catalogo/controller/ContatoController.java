package br.com.unicsul.catalogo.controller;

import br.com.unicsul.catalogo.domain.Contato;
import br.com.unicsul.catalogo.service.ContatoService;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/contato")
public class ContatoController {

    private final ContatoService contatoService;

    public ContatoController(ContatoService contatoService) {
        this.contatoService = contatoService;
    }

    @GetMapping
    public String mostrarFormulario(Model model) {
        if (!model.containsAttribute("contato")) {
            model.addAttribute("contato", new Contato());
        }
        model.addAttribute("pageTitle", "Contato");
        return "contato/formulario";
    }

    @PostMapping
    public String enviarMensagem(@Valid @ModelAttribute("contato") Contato contato,
                               BindingResult result,
                               RedirectAttributes attributes,Model model) {
        if (result.hasErrors()) {
            return "contato/formulario";
        }

        model.addAttribute("pageTitle", "Contato");
        
        contatoService.salvar(contato);
        attributes.addFlashAttribute("mensagem", "Mensagem enviada com sucesso! Entraremos em contato em breve.");
        return "redirect:/contato";
    }
}
