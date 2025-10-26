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
@Table(name = "autor")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_autor_aut")
    private Long id;

    @Column(name = "nm_autor_aut", nullable = false, length = 255)
    private String nome;

    @Column(name = "ds_autor_aut", length = 255)
    private String descricao;

    @Column(name = "dt_criaca_aut", updatable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(name = "dt_altera_aut")
    private LocalDateTime dataAlteracao = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        this.dataAlteracao = LocalDateTime.now();
    }
}
