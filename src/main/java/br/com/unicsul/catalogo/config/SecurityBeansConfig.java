package br.com.unicsul.catalogo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityBeansConfig {

    @Value("${app.security.password-encoder.secret:defaultSecretForDevelopment}")
    private String secret;

    @Bean
    @Profile("!dev")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Profile("dev")
    public PasswordEncoder devPasswordEncoder() {
        // Usando BCrypt com força 4 (mais rápido para desenvolvimento)
        // Isso garante que a mesma senha gere o mesmo hash entre reinicializações
        return new BCryptPasswordEncoder(4);
    }
}
