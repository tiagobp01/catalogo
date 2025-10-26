package br.com.unicsul.catalogo.controller;

import br.com.unicsul.catalogo.domain.Categoria;
import br.com.unicsul.catalogo.domain.Produto;
import br.com.unicsul.catalogo.service.CategoriaService;
import br.com.unicsul.catalogo.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/categoria")
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final ProdutoService produtoService;

    @Autowired
    public CategoriaController(CategoriaService categoriaService, ProdutoService produtoService) {
        this.categoriaService = categoriaService;
        this.produtoService = produtoService;
    }

    @GetMapping("/{categoriaId}")
    public String listarProdutosPorCategoria(@PathVariable Long categoriaId, Model model) {
        // Busca a categoria para mostrar o nome na página
        Categoria categoria = categoriaService.buscarPorId(categoriaId).orElse(null);

        if(categoria == null)
            throw new IllegalArgumentException("Categoria inválida: " + categoriaId);

        // Busca os produtos da categoria
        List<Produto> produtos = produtoService.listarProdutosPorCategoria(categoriaId);

        // Adiciona os atributos ao modelo
        model.addAttribute("categoria", categoria);
        model.addAttribute("produtos", produtos);
        model.addAttribute("pageTitle", "Produtos - " + categoria.getNome());

        return "produtos/lista";
    }
}
