package cl.sterbe.app.infrastructure.controllers;

import cl.sterbe.app.domains.exceptions.WebExchangeException;
import cl.sterbe.app.domains.models.email.EmailMapper;
import cl.sterbe.app.domains.models.users.User;
import cl.sterbe.app.domains.ports.in.users.UserUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserUseCase userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public Flux<User> getUsers(){
        return this.userService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public Mono<User> getUser(@PathVariable String id){
        return this.userService.findById(id);
    }

    @PutMapping("/update-password")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public Mono<User> updatePassword(@Valid @RequestBody Mono<EmailMapper> emailMappe){
        return emailMappe
                .flatMap(e -> this.userService.updatePassword(e))
                .onErrorResume(error -> error instanceof WebExchangeBindException
                            ?Mono.error(new WebExchangeException(Mono.just(error)))
                            :Mono.error(error));
    }
}
