package br.com.unicsul.catalogo.service;

import br.com.unicsul.catalogo.domain.Contato;
import br.com.unicsul.catalogo.repository.ContatoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ContatoService {

    private final ContatoRepository contatoRepository;

    public ContatoService(ContatoRepository contatoRepository) {
        this.contatoRepository = contatoRepository;
    }

    @Transactional(readOnly = true)
    public Page<Contato> listarTodos(Pageable pageable) {
        return contatoRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Contato> buscarPorTermo(String termo, Pageable pageable) {
        return contatoRepository.findByNomeContainingIgnoreCaseOrEmailContainingIgnoreCaseOrMensagemContainingIgnoreCase(
            termo, termo, termo, pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Contato> buscarPorId(Long id) {
        return contatoRepository.findById(id);
    }

    @Transactional
    public Contato salvar(Contato contato) {
        return contatoRepository.save(contato);
    }
}
