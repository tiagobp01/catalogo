package br.com.unicsul.catalogo.repository;

import br.com.unicsul.catalogo.domain.Diretor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiretorRepository extends JpaRepository<Diretor, Long> {
    
    @Query("SELECT d FROM Diretor d WHERE " +
           "LOWER(d.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(d.descricao) LIKE LOWER(CONCAT('%', :termo, '%'))")
    Page<Diretor> buscarPorTermo(@Param("termo") String termo, Pageable pageable);

    @Query("SELECT d FROM Diretor d ")
    List<Diretor> findAllAtivos();
}
