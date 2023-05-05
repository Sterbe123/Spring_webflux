package cl.sterbe.app.controllers;

import cl.sterbe.app.documents.dto.email.EmailMapper;
import cl.sterbe.app.documents.models.users.User;
import cl.sterbe.app.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Mono<ResponseEntity<Mono<User>>> login(@RequestBody EmailMapper emailMapper, ServerWebExchange serverWebExchange) {

        return Mono.just(ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.userService.login(emailMapper, serverWebExchange)));
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<Mono<User>>> register(@RequestBody User user){

        return Mono.just(ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.userService.register(user)));
    }
}
