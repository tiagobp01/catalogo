package br.com.unicsul.catalogo.repository;

import br.com.unicsul.catalogo.domain.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByCategoriaIdAndAtivoTrueOrderByNomeAsc(Long categoriaId);
    
    @Query("SELECT COUNT(p) > 0 FROM Produto p WHERE p.autor.id = :autorId")
    boolean existsByAutorId(@Param("autorId") Long autorId);
    
    @Query("SELECT p FROM Produto p WHERE p.categoria.id = :categoriaId AND p.ativo = true AND p.id != :excluirId ORDER BY p.nome ASC")
    List<Produto> findByCategoriaIdAndIdNotAndAtivoTrue(
        @Param("categoriaId") Long categoriaId,
        @Param("excluirId") Long excluirId,
        Pageable pageable
    );
    
    @Query("SELECT p FROM Produto p " +
           "LEFT JOIN p.categoria c " +
           "LEFT JOIN p.autor a " +
           "LEFT JOIN p.diretor d " +
           "WHERE (LOWER(p.nome) LIKE LOWER(concat('%', :termo, '%')) " +
           "OR LOWER(p.descricao) LIKE LOWER(concat('%', :termo, '%')) " +
           "OR LOWER(c.nome) LIKE LOWER(concat('%', :termo, '%')) " +
           "OR LOWER(a.nome) LIKE LOWER(concat('%', :termo, '%')) " +
           "OR LOWER(d.nome) LIKE LOWER(concat('%', :termo, '%'))) " +
           "AND p.ativo = true")
    Page<Produto> search(@Param("termo") String termo, Pageable pageable);

    @Query("SELECT COUNT(p) > 0 FROM Produto p WHERE p.diretor.id = :diretorId")
    boolean existsByDiretorId(@Param("diretorId") Long diretorId);
    
    @Query("SELECT p FROM Produto p " +
           "WHERE (:categoriaId IS NULL OR p.categoria.id = :categoriaId) " +
           "AND (:autorId IS NULL OR p.autor.id = :autorId) " +
           "AND (:diretorId IS NULL OR p.diretor.id = :diretorId) " +
           "AND p.ativo = true")
    Page<Produto> findByFilters(
        @Param("categoriaId") Long categoriaId,
        @Param("autorId") Long autorId,
        @Param("diretorId") Long diretorId,
        Pageable pageable
    );
}
