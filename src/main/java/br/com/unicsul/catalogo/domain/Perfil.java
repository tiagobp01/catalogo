package br.com.unicsul.catalogo.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "perfil")
public class Perfil {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_perfil_per")
    private Long id;
    
    @Column(name = "nm_perfil_per", nullable = false, unique = true)
    private String nome;
    
    @Column(name = "fl_ativo_per", nullable = false)
    private boolean ativo = true;
    
    @OneToMany(mappedBy = "perfil", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UsuarioPerfil> usuarios = new HashSet<>();
}
