package br.com.unicsul.catalogo.controller.admin;

import br.com.unicsul.catalogo.domain.Autor;
import br.com.unicsul.catalogo.service.AutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Collections;
import java.util.Map;

@Controller
@RequestMapping("/admin/autores")
@PreAuthorize("hasRole('ADMIN')")
public class AutorController {

    @Autowired
    private AutorService autorService;

    @GetMapping("")
    public String listarAutores(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome,asc") String sort,
            @RequestParam(required = false) String search,
            Model model) {
        
        String[] sortParams = sort.split(",");
        Sort.Direction direction = sortParams[1].equalsIgnoreCase("desc") ? 
            Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sortObj = Sort.by(direction, sortParams[0]);
        Pageable pageable = PageRequest.of(page, size, sortObj);
        
        Page<Autor> autoresPage;
        if (search != null && !search.trim().isEmpty()) {
            autoresPage = autorService.buscarPorTermo(search, pageable);
        } else {
            autoresPage = autorService.listarTodos(pageable);
        }
        
        model.addAttribute("pageTitle", "Gerenciar Autores");
        model.addAttribute("activePage", "autores");
        model.addAttribute("autores", autoresPage);
        model.addAttribute("searchTerm", search != null ? search : "");
        model.addAttribute("sortField", sortParams[0]);
        model.addAttribute("sortDirection", sortParams[1]);
        
        return "admin/autores/lista";
    }
    
    @GetMapping("/novo")
    public String exibirFormularioNovo(Model model) {
        model.addAttribute("pageTitle", "Novo Autor");
        model.addAttribute("activePage", "autores");
        model.addAttribute("autor", new Autor());
        return "admin/autores/form";
    }
    
    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable Long id, Model model) {
        return autorService.buscarPorId(id)
            .map(autor -> {
                model.addAttribute("pageTitle", "Editar Autor");
                model.addAttribute("activePage", "autores");
                model.addAttribute("autor", autor);
                return "admin/autores/form";
            })
            .orElse("redirect:/admin/autores");
    }
    
    @PostMapping("/salvar")
    public String salvarAutor(@ModelAttribute("autor") Autor autor, RedirectAttributes redirectAttributes) {
        try {
            autorService.salvar(autor);
            redirectAttributes.addFlashAttribute("mensagemSucesso", 
                String.format("Autor %s salvo com sucesso!", autor.getNome()));
            return "redirect:/admin/autores";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", 
                "Erro ao salvar o autor: " + e.getMessage());
            return "redirect:/admin/autores/novo";
        }
    }
    
    @PostMapping("/excluir/{id}")
    public String excluirAutor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            if (autorService.temProdutosAssociados(id)) {
                redirectAttributes.addFlashAttribute("mensagemAviso", 
                    "Não é possível excluir o autor pois existem produtos associados a ele.");
            } else {
                autorService.excluir(id);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Autor excluído com sucesso!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", 
                "Erro ao excluir o autor: " + e.getMessage());
        }
        return "redirect:/admin/autores";
    }
    
    @GetMapping("/verificar-produtos/{id}")
    @ResponseBody
    public Map<String, Boolean> verificarProdutosAssociados(@PathVariable Long id) {
        boolean temProdutos = autorService.temProdutosAssociados(id);
        return Collections.singletonMap("temProdutos", temProdutos);
    }
}
