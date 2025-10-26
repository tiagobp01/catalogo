package br.com.unicsul.catalogo.controller;

import br.com.unicsul.catalogo.domain.Categoria;
import br.com.unicsul.catalogo.domain.Produto;
import br.com.unicsul.catalogo.service.CategoriaService;
import br.com.unicsul.catalogo.service.ProdutoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/produto")
public class ProdutoController {

    private final ProdutoService produtoService;
    private final CategoriaService categoriaService;

    public ProdutoController(ProdutoService produtoService, CategoriaService categoriaService) {
        this.produtoService = produtoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping("/{categoriaId}/produtos")
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

    @GetMapping("/detalhes/{id}")
    public String detalhesProduto(@PathVariable Long id, Model model) {
        // Busca o produto pelo ID
        Produto produto = produtoService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + id));
        
        // Busca produtos relacionados (mesma categoria, excluindo o produto atual)
        List<Produto> produtosRelacionados = produtoService
            .listarProdutosRelacionados(produto.getCategoria().getId(), id, 3);
        
        // Adiciona os atributos ao modelo
        model.addAttribute("produto", produto);
        model.addAttribute("produtosRelacionados", produtosRelacionados);
        model.addAttribute("pageTitle", produto.getNome() + " - Detalhes");
        
        return "produtos/detalhes";
    }

    /**
     * Realiza uma busca por produtos, filtrando pelo termo de busca,
     * número da página e tamanho da página.
     *
     * @param termo Termo de busca (opcional, padrão vazio)
     * @param page Número da página (opcional, padrão 0)
     * @param size Tamanho da página (opcional, padrão 5)
     * @param model Modelo de dados para a página
     * @return Nome da view para a página de resultados da busca
     */
    @GetMapping("/buscar")
    public String buscarProdutos(
            @RequestParam(value = "termo", required = true, defaultValue = "") String termo,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            Model model) {
        
        // Valida o tamanho da página (5, 20 ou 50)
        if (size != 5 && size != 20 && size != 50) {
            size = 5; // Valor padrão se for diferente dos permitidos
        }
        
        Page<Produto> paginaProdutos = produtoService.search(termo, page, size);
        
        model.addAttribute("produtos", paginaProdutos.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paginaProdutos.getTotalPages());
        model.addAttribute("totalItems", paginaProdutos.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("searchTerm", termo);
        model.addAttribute("pageTitle", "Resultados da busca: " + termo);
        
        return "produtos/busca";
    }
}
