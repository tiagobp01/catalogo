package br.com.unicsul.catalogo.repository;

import br.com.unicsul.catalogo.domain.Contato;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContatoRepository extends JpaRepository<Contato, Long> {
    
    Page<Contato> findByNomeContainingIgnoreCaseOrEmailContainingIgnoreCaseOrMensagemContainingIgnoreCase(
        String nome, String email, String mensagem, Pageable pageable);
}
