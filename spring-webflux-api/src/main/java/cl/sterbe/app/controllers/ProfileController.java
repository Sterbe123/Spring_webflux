package cl.sterbe.app.controllers;

import cl.sterbe.app.documents.models.profiles.Profile;
import cl.sterbe.app.exceptions.CustomListException;
import cl.sterbe.app.services.profiles.ProfileService;
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
    private ProfileService profileService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<Profile> getProfiles(){
        return this.profileService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Profile> getProfile(@PathVariable String id){
        return this.profileService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Profile> save(@Valid @RequestBody Mono<Profile> profile){
        return profile
                .flatMap(p -> this.profileService.save(p))
                .onErrorResume(error -> Mono.error(new CustomListException(Mono.just(error)
                        .cast(WebExchangeBindException.class))));
    }
}
