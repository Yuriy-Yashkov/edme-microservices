package ru.edme.security;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

public class JwtAuthConverter extends JwtAuthenticationConverter {

    public JwtAuthConverter() {
        JwtGrantedAuthoritiesConverter delegate = new JwtGrantedAuthoritiesConverter();
        // Не добавляем префикс, так как в Keycloak он уже есть (ROLE_XXX)
        delegate.setAuthorityPrefix("");
        delegate.setAuthoritiesClaimName("realm_access.roles");

//        this.setJwtGrantedAuthoritiesConverter(new Converter<Jwt, Collection<GrantedAuthority>>() {
//            @Override
//            public Collection<GrantedAuthority> convert(Jwt jwt) {
//                return delegate.convert(jwt);
//            }
//        });
        setJwtGrantedAuthoritiesConverter(delegate);
    }
}
