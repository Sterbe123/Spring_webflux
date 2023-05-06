package cl.sterbe.app.controllers;

import cl.sterbe.app.documents.models.users.User;
import cl.sterbe.app.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public Mono<ResponseEntity<Flux<User>>> getUsers(){
        return Mono.just(ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.userService.findAll()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public Mono<ResponseEntity<Mono<User>>> getUser(@PathVariable String id){
        return Mono.just(ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.userService.findById(id)));
    }
}
