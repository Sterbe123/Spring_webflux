package cl.sterbe.app.infrastructure.controllers;

import cl.sterbe.app.domains.exceptions.WebExchangeException;
import cl.sterbe.app.domains.models.profiles.Profile;
import cl.sterbe.app.domains.ports.in.profiles.ProfileUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/profiles")
public class ProfileController {

    @Autowired
    private ProfileUseCase profileUseCase;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<Profile> getProfiles(){
        return this.profileUseCase.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Profile> getProfile(@PathVariable String id){
        return this.profileUseCase.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Profile> save(@Valid @RequestBody Mono<Profile> profile){
        return profile
                .flatMap(p -> this.profileUseCase.save(p))
                .onErrorResume(error -> error instanceof WebExchangeBindException
                        ?Mono.error(new WebExchangeException(Mono.just(error)))
                        :Mono.error(error));
    }
}
