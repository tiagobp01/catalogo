package br.com.unicsul.catalogo.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@Entity
@Table(name = "usuario_perfil")
@EqualsAndHashCode(of = "id")
public class UsuarioPerfil implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuper_upe")
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuari_usu", nullable = false)
    private Usuario usuario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_perfil_per", nullable = false)
    private Perfil perfil;
    
    // Construtor para facilitar a criação da associação
    public UsuarioPerfil(Usuario usuario, Perfil perfil) {
        this.usuario = usuario;
        this.perfil = perfil;
    }
    
    // Construtor padrão necessário para o JPA
    public UsuarioPerfil() {
    }
}
