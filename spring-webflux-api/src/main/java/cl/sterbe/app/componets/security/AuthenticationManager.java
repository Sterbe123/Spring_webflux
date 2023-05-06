package cl.sterbe.app.componets.security;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication)
                .flatMap(auth -> TokenUtils.authenticationToken(auth.getCredentials().toString()))
                .map(claims -> new UsernamePasswordAuthenticationToken(
                                claims.getSubject(),
                                null,
                                Stream.of(claims.get("authorities"))
                                        .map(r -> (List<Map<String, String>>) r)
                                        .flatMap(role -> role.stream()
                                                .map(ro -> ro.get("authority"))
                                                .map(SimpleGrantedAuthority::new))
                                        .collect(Collectors.toList())));
    }
}
