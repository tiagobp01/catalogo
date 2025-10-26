package br.com.unicsul.catalogo.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "usuario")
public class Usuario {
    
    @Id
    @Column(name = "id_usuari_usu")
    private UUID id;
    
    @Column(name = "nm_usuari_usu", nullable = false)
    private String nome;
    
    @Column(name = "ds_email_usu", nullable = false, unique = true)
    private String email;
    
    @Column(name = "ds_senha_usu", nullable = false)
    private String senha;
    
    @Column(name = "fl_ativo_usu", nullable = false)
    private boolean ativo = true;
    
    @CreationTimestamp
    @Column(name = "dt_criaca_usu", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;
    
    @UpdateTimestamp
    @Column(name = "dt_atuali_usu", nullable = false)
    private LocalDateTime dataAtualizacao;
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UsuarioPerfil> perfis = new HashSet<>();
    
    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}
