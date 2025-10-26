package br.com.unicsul.catalogo.service;

import br.com.unicsul.catalogo.domain.*;
import br.com.unicsul.catalogo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final AutorRepository autorRepository;
    private final DiretorRepository diretorRepository;

    @Autowired
    public ProdutoService(ProdutoRepository produtoRepository, 
                         CategoriaRepository categoriaRepository,
                         AutorRepository autorRepository,
                         DiretorRepository diretorRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
        this.autorRepository = autorRepository;
        this.diretorRepository = diretorRepository;
    }

    public List<Produto> listarProdutosPorCategoria(Long categoriaId) {
        return produtoRepository.findByCategoriaIdAndAtivoTrueOrderByNomeAsc(categoriaId);
    }

    /**
     * Lista produtos de uma categoria específica, excluindo um produto específico
     *
     * @param categoriaId ID da categoria
     * @param excluirId   ID do produto a ser excluído dos resultados
     * @param limite      Número máximo de produtos a retornar
     * @return Lista de produtos da categoria, excluindo o produto com o ID especificado
     */
    public List<Produto> listarProdutosRelacionados(Long categoriaId, Long excluirId, int limite) {
        Pageable pageable = PageRequest.of(0, limite);
        return produtoRepository.findByCategoriaIdAndIdNotAndAtivoTrue(
                categoriaId,
                excluirId,
                pageable
        );
    }
    
    public Optional<Produto> findById(Long id) {
        return produtoRepository.findById(id);
    }
    
    /**
     * Busca produtos por um termo de busca, pesquisando em nome, descrição, categoria e autores
     * 
     * @param termo Termo de busca
     * @param page Número da página (começando em 0)
     * @param size Quantidade de itens por página
     * @return Página de produtos que correspondem à busca
     */
    public Page<Produto> search(String termo, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return produtoRepository.search(termo, pageable);
    }
    
    public Page<Produto> listarTodos(Pageable pageable) {
        return produtoRepository.findAll(pageable);
    }
    
    public Page<Produto> buscarPorTermo(String termo, Long categoriaId, Long autorId, Long diretorId, Pageable pageable) {
        if (termo != null && !termo.trim().isEmpty()) {
            return produtoRepository.search(termo, pageable);
        } else if (categoriaId != null || autorId != null || diretorId != null) {
            return produtoRepository.findByFilters(categoriaId, autorId, diretorId, pageable);
        } else {
            return listarTodos(pageable);
        }
    }
    
    public Page<Produto> buscarPorFiltros(Long categoriaId, Long autorId, Long diretorId, Pageable pageable) {
        return produtoRepository.findByFilters(categoriaId, autorId, diretorId, pageable);
    }
    
    @Transactional
    public Produto salvar(Produto produto) {
        if (produto.getId() == null) {
            produto.setDataCriacao(LocalDateTime.now());
        } else {
            produto.setDataAtualizacao(LocalDateTime.now());
        }
        return produtoRepository.save(produto);
    }
    
    @Transactional
    public void excluir(Long id) {
        produtoRepository.deleteById(id);
    }
    
    public List<Categoria> listarTodasCategorias() {
        return categoriaRepository.findAll();
    }
    
    public List<Autor> listarTodosAutores() {
        return autorRepository.findAll();
    }
    
    public List<Diretor> listarTodosDiretores() {
        return diretorRepository.findAll();
    }
    
    public Optional<Categoria> buscarCategoriaPorId(Long id) {
        return categoriaRepository.findById(id);
    }
    
    public Optional<Autor> buscarAutorPorId(Long id) {
        return autorRepository.findById(id);
    }
    
    public Optional<Diretor> buscarDiretorPorId(Long id) {
        return diretorRepository.findById(id);
    }
}