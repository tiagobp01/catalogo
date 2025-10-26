package br.com.unicsul.catalogo.service;

import br.com.unicsul.catalogo.domain.Autor;
import br.com.unicsul.catalogo.repository.AutorRepository;
import br.com.unicsul.catalogo.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;
    
    @Autowired
    private ProdutoRepository produtoRepository;
    
    public Page<Autor> listarTodos(Pageable pageable) {
        return autorRepository.findAll(pageable);
    }
    
    public Page<Autor> buscarPorTermo(String termo, Pageable pageable) {
        return autorRepository.buscarPorTermo(termo, pageable);
    }
    
    public Optional<Autor> buscarPorId(Long id) {
        return autorRepository.findById(id);
    }
    
    @Transactional
    public Autor salvar(Autor autor) {
        if (autor.getId() == null) {
            autor.setDataCriacao(java.time.LocalDateTime.now());
        }
        autor.setDataAlteracao(java.time.LocalDateTime.now());
        return autorRepository.save(autor);
    }
    
    @Transactional
    public void excluir(Long id) {
        // Verifica se existem produtos associados ao autor
        if (produtoRepository.existsByAutorId(id)) {
            throw new RuntimeException("Não é possível excluir o autor pois existem produtos associados a ele.");
        }
        autorRepository.deleteById(id);
    }
    
    public boolean temProdutosAssociados(Long autorId) {
        return produtoRepository.existsByAutorId(autorId);
    }

    public List<Autor> listarAtivos() {
        return autorRepository.findAllAtivos();
    }
}
