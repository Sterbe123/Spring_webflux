package cl.sterbe.app.componets.security;

import cl.sterbe.app.exceptions.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private TokenUtils tokenUtils;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication)
                .map(auth -> this.tokenUtils.authenticationToken(auth.getCredentials().toString()))
                .log()
                .onErrorResume(error -> Mono.error(new CustomException("bad token", HttpStatus.UNAUTHORIZED)))
                .map(claims -> new UsernamePasswordAuthenticationToken(
                        claims.getSubject(),
                        null,
                        Stream.of(claims.get("authorities"))
                                .map(r -> (List<Map<String,String>>) r)
                                .flatMap(role -> role.stream()
                                        .map(ro -> ro.get("authority"))
                                        .map(SimpleGrantedAuthority::new))
                                .collect(Collectors.toList())
                ));
    }
}
