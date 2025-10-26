package br.com.unicsul.catalogo.controller.admin;

import br.com.unicsul.catalogo.domain.Autor;
import br.com.unicsul.catalogo.domain.Categoria;
import br.com.unicsul.catalogo.domain.Diretor;
import br.com.unicsul.catalogo.domain.Produto;
import br.com.unicsul.catalogo.service.AutorService;
import br.com.unicsul.catalogo.service.CategoriaService;
import br.com.unicsul.catalogo.service.DiretorService;
import br.com.unicsul.catalogo.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/admin/produtos")
@PreAuthorize("hasRole('ADMIN')")
public class ProdutoAdminController {

    private final ProdutoService produtoService;
    private final CategoriaService categoriaService;
    private final AutorService autorService;
    private final DiretorService diretorService;

    @Autowired
    public ProdutoAdminController(ProdutoService produtoService,
                                 CategoriaService categoriaService,
                                 AutorService autorService,
                                 DiretorService diretorService) {
        this.produtoService = produtoService;
        this.categoriaService = categoriaService;
        this.autorService = autorService;
        this.diretorService = diretorService;
    }

    @GetMapping("")
    public String listarProdutos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome,asc") String sort,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Long autorId,
            @RequestParam(required = false) Long diretorId,
            Model model) {
        
        String[] sortParams = sort.split(",");
        Sort.Direction direction = sortParams[1].equalsIgnoreCase("desc") ? 
            Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sortObj = Sort.by(direction, sortParams[0]);
        Pageable pageable = PageRequest.of(page, size, sortObj);
        
        Page<Produto> produtosPage = produtoService.buscarPorTermo(search, categoriaId, autorId, diretorId, pageable);
        
        model.addAttribute("pageTitle", "Gerenciar Produtos");
        model.addAttribute("activePage", "produtos");
        model.addAttribute("produtos", produtosPage);
        model.addAttribute("categorias", categoriaService.listarAtivas());
        model.addAttribute("autores", autorService.listarAtivos());
        model.addAttribute("diretores", diretorService.listarAtivos());
        
        // Manter os parâmetros de filtro
        model.addAttribute("search", search);
        model.addAttribute("categoriaId", categoriaId);
        model.addAttribute("autorId", autorId);
        model.addAttribute("diretorId", diretorId);
        
        return "admin/produtos/lista";
    }

    @GetMapping("/novo")
    public String mostrarFormularioNovo(Model model) {
        model.addAttribute("produto", new Produto());
        carregarDadosFormulario(model);
        return "admin/produtos/form";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicao(@PathVariable Long id, Model model) {
        Produto produto = produtoService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ID de produto inválido: " + id));
        
        model.addAttribute("produto", produto);
        carregarDadosFormulario(model);
        return "admin/produtos/form";
    }

    @PostMapping("/salvar")
    public String salvarProduto(
            @ModelAttribute("produto") Produto produto,
            @RequestParam(value = "imagem", required = false) MultipartFile imagem,
            @RequestParam(value = "dataLancamentoStr", required = false) String dataLancamentoStr,
            RedirectAttributes attributes) throws IOException {
        
        // Processar data de lançamento
        if (dataLancamentoStr != null && !dataLancamentoStr.isEmpty()) {
            try {
                LocalDate data = LocalDate.parse(dataLancamentoStr);
                produto.setDataLancamento(data.atStartOfDay());
            } catch (Exception e) {
                // Se houver erro na conversão, mantém a data atual
                produto.setDataLancamento(LocalDateTime.now());
            }
        } else {
            // Se não houver data fornecida, usa a data atual
            produto.setDataLancamento(LocalDateTime.now());
        }
        
        // Processar imagem se fornecida
        if (imagem != null && !imagem.isEmpty()) {
            String imagemBase64 = "data:" + imagem.getContentType() + ";base64," + Base64.getEncoder().encodeToString(imagem.getBytes());
            produto.setFoto(imagemBase64);
        } else if(produto.getId() !=null ){
            if (produto.getFoto() == null) {
                produtoService.findById(produto.getId())
                        .ifPresent(produtoExistente ->
                                produto.setFoto(produtoExistente.getFoto())
                        );
            }
        }
        
        produtoService.salvar(produto);
        attributes.addFlashAttribute("mensagem", "Produto salvo com sucesso!");
        return "redirect:/admin/produtos";
    }

    @PostMapping("/excluir/{id}")
    public String excluirProduto(@PathVariable Long id, RedirectAttributes attributes) {
        produtoService.excluir(id);
        attributes.addFlashAttribute("mensagem", "Produto excluído com sucesso!");
        return "redirect:/admin/produtos";
    }

    private void carregarDadosFormulario(Model model) {
        model.addAttribute("categorias", categoriaService.listarAtivas());
        model.addAttribute("autores", autorService.listarAtivos());
        model.addAttribute("diretores", diretorService.listarAtivos());
    }

    // Endpoints para busca AJAX
    @GetMapping("/buscar-categorias")
    @ResponseBody
    public List<Categoria> buscarCategorias(@RequestParam String termo) {
        return categoriaService.buscarPorTermo(termo, PageRequest.of(0, 10)).getContent();
    }

    @GetMapping("/buscar-autores")
    @ResponseBody
    public List<Autor> buscarAutores(@RequestParam String termo) {
        return autorService.buscarPorTermo(termo, PageRequest.of(0, 10)).getContent();
    }

    @GetMapping("/buscar-diretores")
    @ResponseBody
    public List<Diretor> buscarDiretores(@RequestParam String termo) {
        return diretorService.buscarPorTermo(termo, PageRequest.of(0, 10)).getContent();
    }
}
