package br.com.unicsul.catalogo.service;

import br.com.unicsul.catalogo.domain.Categoria;
import br.com.unicsul.catalogo.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    /**
     * Lista todas as categorias ordenadas por nome
     */
    public List<Categoria> listarTodasCategorias() {
        return categoriaRepository.findAllByOrderByNomeAsc();
    }

    /**
     * Busca uma categoria por ID
     * @param id ID da categoria
     * @return Optional contendo a categoria, se encontrada
     */
    public Optional<Categoria> buscarPorId(Long id) {
        return categoriaRepository.findById(id);
    }
    
    /**
     * Busca uma categoria por ID ou lança uma exceção se não encontrada
     * @param id ID da categoria
     * @return Categoria encontrada
     * @throws RuntimeException se a categoria não for encontrada
     */
    public Categoria buscarPorIdOuFalhe(Long id) {
        return buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada com o ID: " + id));
    }

    /**
     * Salva uma nova categoria ou atualiza uma existente
     */
    public Categoria salvar(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    /**
     * Exclui uma categoria por ID
     */
    public void excluir(Long id) {
        categoriaRepository.deleteById(id);
    }
    
    /**
     * Lista todas as categorias ativas ordenadas por nome
     * @return Lista de categorias ativas ordenadas
     */
    public List<Categoria> listarAtivas() {
        return categoriaRepository.findByAtivoTrueOrderByNomeAsc();
    }
    
    /**
     * Busca categorias paginadas
     */
    public Page<Categoria> listarTodos(Pageable pageable) {
        return categoriaRepository.findAll(pageable);
    }
    
    /**
     * Busca categorias por termo de busca
     */
    public Page<Categoria> buscarPorTermo(String termo, Pageable pageable) {
        return categoriaRepository.findByNomeContainingIgnoreCase(termo, pageable);
    }
    
    
    /**
     * Verifica se existem produtos associados a uma categoria
     */
    public boolean temProdutosAssociados(Long categoriaId) {
        return categoriaRepository.countProdutosByCategoriaId(categoriaId) > 0;
    }
}
