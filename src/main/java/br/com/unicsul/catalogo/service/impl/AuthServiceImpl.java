package br.com.unicsul.catalogo.service.impl;

import br.com.unicsul.catalogo.domain.Perfil;
import br.com.unicsul.catalogo.domain.Usuario;
import br.com.unicsul.catalogo.domain.UsuarioPerfil;
import br.com.unicsul.catalogo.dto.UsuarioRegistroDTO;
import br.com.unicsul.catalogo.repository.PerfilRepository;
import br.com.unicsul.catalogo.repository.UsuarioPerfilRepository;
import br.com.unicsul.catalogo.repository.UsuarioRepository;
import br.com.unicsul.catalogo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final UsuarioPerfilRepository usuarioPerfilRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(UsuarioRepository usuarioRepository,
                          PerfilRepository perfilRepository,
                          UsuarioPerfilRepository usuarioPerfilRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.usuarioPerfilRepository = usuarioPerfilRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("\n=== AuthServiceImpl.loadUserByUsername ===");
        System.out.println("Buscando usuário com email: " + email);
        
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> {
                    String errorMsg = "Usuário não encontrado com o e-mail: " + email;
                    System.out.println("ERRO: " + errorMsg);
                    return new UsernameNotFoundException(errorMsg);
                });
                
        System.out.println("Usuário encontrado:");
        System.out.println("- Nome: " + usuario.getNome());
        System.out.println("- ID: " + usuario.getId());
        System.out.println("- Ativo: " + usuario.isAtivo());
        System.out.println("- Senha armazenada: " + usuario.getSenha());
        System.out.println("- Senha codificada para 'admin123': " + passwordEncoder.encode("admin123"));
        
        if (!usuario.isAtivo()) {
            String errorMsg = "Acesso negado: Usuário está inativo: " + email;
            System.out.println("ERRO: " + errorMsg);
            throw new UsernameNotFoundException(errorMsg);
        }
        
        // Log dos perfis do usuário
        if (usuario.getPerfis() != null && !usuario.getPerfis().isEmpty()) {
            System.out.println("Perfis do usuário " + email + ":");
            usuario.getPerfis().forEach(perfil -> {
                System.out.println(" - ID: " + perfil.getPerfil().getId() + ", Nome: " + perfil.getPerfil().getNome());
            });
        } else {
            System.out.println("AVISO: Usuário " + email + " não possui perfis associados");
        }
        
        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.isAtivo(),
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                usuario.getPerfis().stream()
                        .map(perfil -> {
                            String role = "ROLE_" + perfil.getPerfil().getNome();
                            System.out.println("Adicionando perfil: " + role);
                            return new org.springframework.security.core.authority.SimpleGrantedAuthority(role);
                        })
                        .collect(java.util.stream.Collectors.toList())
        );
    }

    @Override
    @Transactional
    public Usuario registrarNovoUsuario(UsuarioRegistroDTO registroDTO) {
        // Verifica se o e-mail já está em uso
        if (emailExiste(registroDTO.getEmail())) {
            throw new IllegalArgumentException("Já existe uma conta cadastrada com este e-mail");
        }

        // Verifica se as senhas coincidem
        if (!registroDTO.getSenha().equals(registroDTO.getConfirmacaoSenha())) {
            throw new IllegalArgumentException("As senhas não coincidem");
        }

        // Cria o novo usuário
        Usuario usuario = new Usuario();
        usuario.setNome(registroDTO.getNome());
        usuario.setEmail(registroDTO.getEmail());
        usuario.setSenha(passwordEncoder.encode(registroDTO.getSenha()));
        usuario.setAtivo(true);

        // Salva o usuário
        usuario = usuarioRepository.save(usuario);

        // Atribui o perfil padrão (ROLE_USER)
        Perfil perfil = perfilRepository.findByNome("ROLE_USER")
                .orElseGet(() -> {
                    Perfil novoPerfil = new Perfil();
                    novoPerfil.setNome("ROLE_USER");
                    return perfilRepository.save(novoPerfil);
                });

        // Associa o perfil ao usuário
        UsuarioPerfil usuarioPerfil = new UsuarioPerfil();
        usuarioPerfil.setUsuario(usuario);
        usuarioPerfil.setPerfil(perfil);
        usuarioPerfilRepository.save(usuarioPerfil);

        return usuario;
    }

    @Override
    public boolean emailExiste(String email) {
        return usuarioRepository.existsByEmail(email);
    }
}
