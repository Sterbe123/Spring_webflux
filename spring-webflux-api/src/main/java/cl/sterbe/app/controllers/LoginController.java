package cl.sterbe.app.controllers;

import cl.sterbe.app.documents.dto.email.EmailMapper;
import cl.sterbe.app.documents.models.users.User;
import cl.sterbe.app.exceptions.WebExchangeException;
import cl.sterbe.app.services.users.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public Mono<User> login(@Valid @RequestBody Mono<EmailMapper> emailMapper, ServerWebExchange serverWebExchange) {
        return emailMapper
                .flatMap(email -> this.userService.login(email, serverWebExchange))
                .onErrorResume(error -> error instanceof WebExchangeBindException
                        ?Mono.error(new WebExchangeException(Mono.just(error)))
                        :Mono.error(error));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Map<String,Object>> register(@Valid @RequestBody Mono<EmailMapper> emailMapper){
        return emailMapper
                .flatMap(email -> this.userService.register(email))
                .onErrorResume(error -> error instanceof WebExchangeBindException
                        ?Mono.error(new WebExchangeException(Mono.just(error)))
                        :Mono.error(error));
    }
}
