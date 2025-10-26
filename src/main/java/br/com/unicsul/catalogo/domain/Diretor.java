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
@Table(name = "diretor")
public class Diretor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_direto_dir")
    private Long id;

    @Column(name = "nm_direto_dir", nullable = false, length = 255)
    private String nome;

    @Column(name = "ds_direto_dir", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "dt_criaca_dir", updatable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(name = "dt_altera_dir")
    private LocalDateTime dataAlteracao = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        this.dataAlteracao = LocalDateTime.now();
    }
}
