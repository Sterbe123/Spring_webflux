package cl.sterbe.app.advices;

import cl.sterbe.app.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class HandlerException {

    @ExceptionHandler(WebExchangeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<List<String>> webExchangeException(WebExchangeException e){
        return e.getThrowable().cast(WebExchangeBindException.class)
                .flatMap(web -> Mono.just(web.getFieldErrors()))
                .flatMapMany(Flux::fromIterable)
                .map(error -> "El campo " + error.getField() + ", " + error.getDefaultMessage())
                .collectList();
    }

    @ExceptionHandler(SamePasswordsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<Map<String,String>> samePasswords(SamePasswordsException e){
        return Mono.just(Map.of("error", ""));
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<Map<String,String>> userNotFound(){
        return Mono.just(Map.of("error", "user not found"));
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<Map<String,String>> badCredentials(BadCredentialsException e){
        return Mono.just(Map.of("error", "bad credentials"));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<List<String>> exception(Exception e){
        return Mono.just(Collections.singletonList(e.getMessage()));
    }

    @ExceptionHandler(TokenErrorException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Mono<List<String>> tokenError(TokenErrorException e){
        return Mono.just(Collections.singletonList(e.getMessage()));
    }
}
