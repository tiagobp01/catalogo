package br.com.unicsul.catalogo.repository;

import br.com.unicsul.catalogo.domain.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
    // Busca categorias ordenadas por nome
    List<Categoria> findAllByOrderByNomeAsc();
    
    // Busca categorias ativas
    List<Categoria> findByAtivoTrueOrderByNomeAsc();
    
    // Busca categorias paginadas
    Page<Categoria> findAll(Pageable pageable);
    
    // Busca categorias por termo de busca
    Page<Categoria> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
    
    // Conta produtos associados a uma categoria
    @Query("SELECT COUNT(p) FROM Produto p WHERE p.categoria.id = :categoriaId")
    int countProdutosByCategoriaId(@Param("categoriaId") Long categoriaId);
}
