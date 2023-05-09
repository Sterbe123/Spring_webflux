package cl.sterbe.app.controllers;

import cl.sterbe.app.documents.dto.email.EmailMapper;
import cl.sterbe.app.documents.models.users.User;
import cl.sterbe.app.exceptions.CustomException;
import cl.sterbe.app.exceptions.NotUpdateResourceException;
import cl.sterbe.app.exceptions.WebExchangeException;
import cl.sterbe.app.exceptions.SamePasswordsException;
import cl.sterbe.app.services.users.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

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
                .flatMap(e -> SecurityContextHolder.getContext().getAuthentication().getName().equals(e.getEmail())
                            ?Mono.just(e)
                            :Mono.error(new NotUpdateResourceException()))
                .flatMap(e -> this.userService.updatePassword(e))
                .onErrorResume(error -> error instanceof WebExchangeBindException
                        ?Mono.error(new WebExchangeException(Mono.just(error)))
                        :Mono.error(Objects.requireNonNull(CustomException.castException(error).block())));
    }
}
