package ru.edme.gateway.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;

@Configuration
public class JwtDecoderConfig {

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        return ReactiveJwtDecoders.fromIssuerLocation("http://localhost:8180/realms/edme-realm");

//        return NimbusReactiveJwtDecoder.withJwkSetUri("http://localhost:8180/realms/edme-realm/protocol/openid-connect/certs")
//                .build();
    }
}
