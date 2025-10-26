package br.com.unicsul.catalogo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produt_pro")
    private Long id;

    @Column(name = "nm_produt_pro", nullable = false, length = 255)
    private String nome;

    @Column(name = "ds_produt_pro", columnDefinition = "TEXT")
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_catego_cat", nullable = false)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_autor_aut", nullable = false)
    private Autor autor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_direto_dir", nullable = false)
    private Diretor diretor;

    @Column(name = "vl_preco_pro", nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(name = "mm_foto_pro", columnDefinition = "TEXT")
    private String foto;

    @Column(name = "fl_ativos_pro")
    private Boolean ativo = true;

    @Column(name = "dt_lancam_pro", nullable = false)
    private LocalDateTime dataLancamento;

    @Column(name = "dt_cadast_pro", updatable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(name = "dt_altera_pro")
    private LocalDateTime dataAtualizacao = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }
}
