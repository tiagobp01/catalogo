package br.com.unicsul.catalogo.controller.admin;

import br.com.unicsul.catalogo.domain.Categoria;
import br.com.unicsul.catalogo.service.CategoriaService;
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
@RequestMapping("/admin/categorias")
@PreAuthorize("hasRole('ADMIN')")
public class CategoriaAdminController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("")
    public String listarCategorias(
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
        
        Page<Categoria> categoriasPage;
        if (search != null && !search.trim().isEmpty()) {
            categoriasPage = categoriaService.buscarPorTermo(search, pageable);
        } else {
            categoriasPage = categoriaService.listarTodos(pageable);
        }
        
        model.addAttribute("pageTitle", "Gerenciar Categorias");
        model.addAttribute("activePage", "categorias");
        model.addAttribute("categorias", categoriasPage);
        model.addAttribute("searchTerm", search != null ? search : "");
        model.addAttribute("sortField", sortParams[0]);
        model.addAttribute("sortDirection", sortParams[1]);
        
        return "admin/categorias/lista";
    }
    
    @GetMapping("/novo")
    public String exibirFormularioNovo(Model model) {
        model.addAttribute("pageTitle", "Nova Categoria");
        model.addAttribute("activePage", "categorias");
        model.addAttribute("categoria", new Categoria());
        return "admin/categorias/form";
    }
    
    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable Long id, Model model) {
        return categoriaService.buscarPorId(id)
            .map(categoria -> {
                model.addAttribute("pageTitle", "Editar Categoria");
                model.addAttribute("activePage", "categorias");
                model.addAttribute("categoria", categoria);
                return "admin/categorias/form";
            })
            .orElse("redirect:/admin/categorias");
    }
    
    @PostMapping("/salvar")
    public String salvarCategoria(@ModelAttribute("categoria") Categoria categoria, 
                                RedirectAttributes redirectAttributes) {
        try {
            categoriaService.salvar(categoria);
            redirectAttributes.addFlashAttribute("mensagemSucesso", 
                String.format("Categoria %s salva com sucesso!", categoria.getNome()));
            return "redirect:/admin/categorias";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", 
                "Erro ao salvar a categoria: " + e.getMessage());
            return "redirect:/admin/categorias/novo";
        }
    }
    
    @PostMapping("/excluir/{id}")
    public String excluirCategoria(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            if (categoriaService.temProdutosAssociados(id)) {
                redirectAttributes.addFlashAttribute("mensagemAviso", 
                    "Não é possível excluir a categoria pois existem produtos associados a ela.");
            } else {
                categoriaService.excluir(id);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Categoria excluída com sucesso!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", 
                "Erro ao excluir a categoria: " + e.getMessage());
        }
        return "redirect:/admin/categorias";
    }
    
    @GetMapping("/verificar-produtos/{id}")
    @ResponseBody
    public Map<String, Boolean> verificarProdutosAssociados(@PathVariable Long id) {
        boolean temProdutos = categoriaService.temProdutosAssociados(id);
        return Collections.singletonMap("temProdutos", temProdutos);
    }
}
