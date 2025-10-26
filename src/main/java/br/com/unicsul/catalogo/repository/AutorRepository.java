package br.com.unicsul.catalogo.repository;

import br.com.unicsul.catalogo.domain.Autor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    
    @Query("SELECT a FROM Autor a WHERE " +
           "LOWER(a.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(a.descricao) LIKE LOWER(CONCAT('%', :termo, '%'))")
    Page<Autor> buscarPorTermo(@Param("termo") String termo, Pageable pageable);

    @Query("SELECT a FROM Autor a ")
    List<Autor> findAllAtivos();
}

