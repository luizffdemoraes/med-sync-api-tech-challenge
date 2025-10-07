package br.com.fiap.postech.medsync.scheduling.infrastructure.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Collection;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // ============================================================
    //  1. Configuração principal de segurança
    // ============================================================
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.POST, "/appointments").hasAnyAuthority("ROLE_NURSE", "ROLE_DOCTOR")
                        .requestMatchers(HttpMethod.GET, "/appointments/**").hasAnyAuthority("ROLE_NURSE", "ROLE_DOCTOR")
                        .requestMatchers(HttpMethod.DELETE, "/appointments/**").hasAuthority("ROLE_DOCTOR")
                        .requestMatchers(HttpMethod.PATCH, "/appointments/**/complete").hasAuthority("ROLE_DOCTOR")
                        .requestMatchers(HttpMethod.PATCH, "/appointments/**/medical-data").hasAuthority("ROLE_DOCTOR")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtConfigurer -> {
                            try {
                                jwtConfigurer.decoder(jwtDecoder())
                                        .jwtAuthenticationConverter(jwtAuthenticationConverter());
                            } catch (Exception e) {
                                throw new RuntimeException("Erro ao configurar JwtDecoder", e);
                            }
                        })
                );

        return http.build();
    }

    // ============================================================
    //  2. Converter de autoridades a partir do JWT
    // ============================================================
    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Collection<String> authorities = jwt.getClaim("authorities");
            if (authorities == null) return java.util.List.of();
            return authorities.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        });
        return converter;
    }

    // ============================================================
    //  3. Configuração do JwtDecoder com chave pública RSA fixa
    // ============================================================
    @Bean
    public JwtDecoder jwtDecoder() throws Exception {
        String publicKeyPEM;

        try (InputStream keyStream = getClass().getClassLoader().getResourceAsStream("rsa/public-key.pem")) {
            if (keyStream == null) {
                throw new IllegalStateException("RSA public key not found in classpath (rsa/public-key.pem)");
            }

            publicKeyPEM = new String(keyStream.readAllBytes())
                    .replaceAll("-----BEGIN (.*)-----", "")
                    .replaceAll("-----END (.*)-----", "")
                    .replaceAll("\\s", "");
        }

        byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(decoded));

        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(publicKey).build();

        // Validação do issuer
        OAuth2TokenValidator<Jwt> issuerValidator = JwtValidators.createDefaultWithIssuer("http://localhost:8079");
        jwtDecoder.setJwtValidator(issuerValidator);

        return jwtDecoder;
    }
}