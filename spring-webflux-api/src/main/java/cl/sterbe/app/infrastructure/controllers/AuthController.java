package cl.sterbe.app.infrastructure.controllers;

import cl.sterbe.app.domains.exceptions.BadCredentialsException;
import cl.sterbe.app.domains.exceptions.TokenErrorException;
import cl.sterbe.app.domains.exceptions.WebExchangeException;
import cl.sterbe.app.domains.models.email.EmailMapper;
import cl.sterbe.app.domains.models.users.User;
import cl.sterbe.app.domains.ports.in.users.UserUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserUseCase userUseCase;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public Mono<User> login(@Valid @RequestBody Mono<EmailMapper> emailMapper, ServerWebExchange serverWebExchange) {
        return emailMapper
                .flatMap(email -> this.userUseCase.login(email, serverWebExchange))
                .onErrorResume(error -> error instanceof WebExchangeBindException
                        ?Mono.error(new WebExchangeException(Mono.just(error)))
                        :Mono.error(error));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Map<String,Object>> register(@Valid @RequestBody Mono<EmailMapper> emailMapper){
        return emailMapper
                .flatMap(email -> this.userUseCase.register(email))
                .onErrorResume(error -> error instanceof WebExchangeBindException
                        ?Mono.error(new WebExchangeException(Mono.just(error)))
                        :Mono.error(error));
    }

    @GetMapping("/validate-account/{token}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<List<String>> validateAccount(@PathVariable String token){
        return Mono.just(token)
                .flatMap(t -> this.userUseCase.validateAccount(t)
                        .map(Collections::singletonList))
                .onErrorResume(error -> Mono.error(new TokenErrorException("bad token")));
    }

    @PostMapping("/validate-account")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Map<String,String>> resendToken(@RequestBody Mono<EmailMapper> email){
        return email
                .flatMap(e -> e.getEmail().isEmpty() || e.getEmail()==null
                        ?Mono.error(new BadCredentialsException()):Mono.just(e))
                .flatMap(e -> this.userUseCase.reSendToken(e.getEmail())
                .onErrorResume(Mono::error));
    }
}
