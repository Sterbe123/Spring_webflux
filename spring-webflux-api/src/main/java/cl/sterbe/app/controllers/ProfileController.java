package cl.sterbe.app.controllers;

import cl.sterbe.app.documents.models.profiles.Profile;
import cl.sterbe.app.services.profiles.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/profiles")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping
    public Mono<ResponseEntity<Flux<Profile>>> getProfiles(){
        return Mono.just(ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.profileService.findAll()));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Mono<Profile>>> getProfile(@PathVariable String id){
        return Mono.just(ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.profileService.findById(id)));
    }
}
