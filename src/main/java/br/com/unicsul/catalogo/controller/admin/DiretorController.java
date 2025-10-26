package br.com.unicsul.catalogo.controller.admin;

import br.com.unicsul.catalogo.domain.Diretor;
import br.com.unicsul.catalogo.service.DiretorService;
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
@RequestMapping("/admin/diretores")
@PreAuthorize("hasRole('ADMIN')")
public class DiretorController {

    @Autowired
    private DiretorService diretorService;

    @GetMapping("")
    public String listarDiretores(
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
        
        Page<Diretor> diretoresPage;
        if (search != null && !search.trim().isEmpty()) {
            diretoresPage = diretorService.buscarPorTermo(search, pageable);
        } else {
            diretoresPage = diretorService.listarTodos(pageable);
        }
        
        model.addAttribute("pageTitle", "Gerenciar Diretores");
        model.addAttribute("activePage", "diretores");
        model.addAttribute("diretores", diretoresPage);
        model.addAttribute("searchTerm", search != null ? search : "");
        model.addAttribute("sortField", sortParams[0]);
        model.addAttribute("sortDirection", sortParams[1]);

        return "admin/diretores/lista";
    }

    @GetMapping("/novo")
    public String exibirFormularioNovo(Model model) {
        model.addAttribute("pageTitle", "Novo Diretor");
        model.addAttribute("activePage", "diretores");
        model.addAttribute("diretor", new Diretor());
        return "admin/diretores/form";
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return diretorService.buscarPorId(id)
            .map(diretor -> {
                model.addAttribute("pageTitle", "Editar Diretor");
                model.addAttribute("activePage", "diretores");
                model.addAttribute("diretor", diretor);
                return "admin/diretores/form";
            })
            .orElseGet(() -> {
                redirectAttributes.addFlashAttribute("errorMessage", "Diretor não encontrado.");
                return "redirect:/admin/diretores";
            });
    }

    @PostMapping("/salvar")
    public String salvarDiretor(@ModelAttribute("diretor") Diretor diretor, RedirectAttributes redirectAttributes) {
        try {
            diretorService.salvar(diretor);
            redirectAttributes.addFlashAttribute("successMessage", "Diretor salvo com sucesso!");
            return "redirect:/admin/diretores";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao salvar o diretor: " + e.getMessage());
            return "redirect:/admin/diretores/novo";
        }
    }

    @PostMapping("/excluir/{id}")
    public String excluirDiretor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            diretorService.excluir(id);
            redirectAttributes.addFlashAttribute("successMessage", "Diretor excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao excluir o diretor: " + e.getMessage());
        }
        return "redirect:/admin/diretores";
    }

    @GetMapping("/verificar-produtos/{id}")
    @ResponseBody
    public Map<String, Boolean> verificarProdutosAssociados(@PathVariable Long id) {
        boolean temProdutos = diretorService.temProdutosAssociados(id);
        return Collections.singletonMap("temProdutos", temProdutos);
    }

}
