package br.com.unicsul.catalogo.config;

import br.com.unicsul.catalogo.config.security.CustomAuthenticationSuccessHandler;
import br.com.unicsul.catalogo.config.security.SessionFilter;
import br.com.unicsul.catalogo.config.security.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import br.com.unicsul.catalogo.service.AuthService;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private UserSession userSession;

    // Adicione UserSession no construtor
    public SecurityConfig(AuthService authService, PasswordEncoder passwordEncoder, UserSession userSession) {
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
        this.userSession = userSession;
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            System.out.println("Falha na autenticação: " + exception.getMessage());
            request.getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION", exception);
            response.sendRedirect("/login?error=true");
        };
    }

    @Bean
    public LogoutHandler logoutHandler() {
        return (request, response, authentication) -> {
            if (userSession != null) {
                userSession.limpar();
            }
            request.getSession().invalidate();
        };
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(authService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Autowired
    private SessionFilter sessionFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("Configurando SecurityFilterChain...");

        http
                .csrf(csrf -> csrf.disable())
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(
                                        "/**",
                                        "/sobre",
                                        "/login",
                                        "/login/**",
                                        "/css/**",
                                        "/js/**",
                                        "/images/**",
                                        "/webjars/**",
                                        "/favicon.ico",
                                        "/h2-console/**"
                                ).permitAll()
                                .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .addFilterBefore(sessionFilter, UsernamePasswordAuthenticationFilter.class)
                .headers(headers ->
                        headers.frameOptions(frame -> frame.sameOrigin())
                )
                .formLogin(login ->
                        login
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .usernameParameter("ds_email_usu")
                                .passwordParameter("ds_senha_usu")
                                .successHandler(authenticationSuccessHandler())
                                .failureHandler(authenticationFailureHandler())
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login?logout")
                                .invalidateHttpSession(true)
                                .clearAuthentication(true)
                                .deleteCookies("JSESSIONID")
                                .addLogoutHandler(logoutHandler())
                )
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
                    session.sessionFixation().migrateSession();
                    session.maximumSessions(1)
                           .maxSessionsPreventsLogin(false)
                           .expiredUrl("/login?expired");
                    session.invalidSessionUrl("/login?invalid-session");
                })
                .exceptionHandling(exception ->
                        exception.accessDeniedPage("/acesso-negado")
                );

        return http.build();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}