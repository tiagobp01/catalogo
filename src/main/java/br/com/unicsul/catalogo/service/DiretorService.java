package br.com.unicsul.catalogo.service;

import br.com.unicsul.catalogo.domain.Diretor;
import br.com.unicsul.catalogo.repository.DiretorRepository;
import br.com.unicsul.catalogo.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DiretorService {

    @Autowired
    private DiretorRepository diretorRepository;
    
    @Autowired
    private ProdutoRepository produtoRepository;
    
    public Page<Diretor> listarTodos(Pageable pageable) {
        return diretorRepository.findAll(pageable);
    }
    
    public Page<Diretor> buscarPorTermo(String termo, Pageable pageable) {
        return diretorRepository.buscarPorTermo(termo, pageable);
    }
    
    public Optional<Diretor> buscarPorId(Long id) {
        return diretorRepository.findById(id);
    }
    
    @Transactional
    public Diretor salvar(Diretor diretor) {
        if (diretor.getId() == null) {
            diretor.setDataCriacao(java.time.LocalDateTime.now());
        }
        diretor.setDataAlteracao(java.time.LocalDateTime.now());
        return diretorRepository.save(diretor);
    }
    
    @Transactional
    public void excluir(Long id) {
        // Verifica se existem produtos associados ao diretor
        if (produtoRepository.existsByDiretorId(id)) {
            throw new RuntimeException("Não é possível excluir o diretor pois existem produtos associados a ele.");
        }
        diretorRepository.deleteById(id);
    }

    public boolean temProdutosAssociados(Long id) {
        return produtoRepository.existsByDiretorId(id);
    }

    public List<Diretor> listarAtivos() {
        return diretorRepository.findAllAtivos();
    }
}
