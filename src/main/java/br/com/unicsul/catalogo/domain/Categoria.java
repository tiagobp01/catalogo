package br.com.unicsul.catalogo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_catego_cat")
    private Long id;

    @Column(name = "nm_catego_cat", nullable = false, length = 255)
    private String nome;

    @Column(name = "ds_catego_cat", length = 255)
    private String descricao;

    @Column(name = "ds_icone_cat", length = 255)
    private String icone;

    @Column(name = "fl_ativos_cat", nullable = false)
    private Boolean ativo = true;

    @Column(name = "dt_criaca_cat", updatable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(name = "dt_altera_cat")
    private LocalDateTime dataAlteracao = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        this.dataAlteracao = LocalDateTime.now();
    }
    
    // Getters e Setters adicionais para compatibilidade com o Thymeleaf
    public String getNmCategoCat() {
        return nome;
    }
    
    public String getDsCategoCat() {
        return descricao;
    }
    
    public String getDsIconeCat() {
        return icone;
    }
}
