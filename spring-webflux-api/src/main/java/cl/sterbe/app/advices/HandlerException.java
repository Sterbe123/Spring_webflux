package cl.sterbe.app.advices;

import cl.sterbe.app.exceptions.CustomException;
import cl.sterbe.app.exceptions.CustomListException;
import cl.sterbe.app.exceptions.TokenError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestControllerAdvice
public class HandlerException {

    @ExceptionHandler(CustomException.class)
    public Mono<ResponseEntity<String>> customException(CustomException e){
        return Mono.just(ResponseEntity
                .status(e.getHttpStatus())
                .body(e.getMessage()));
    }

    @ExceptionHandler(CustomListException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<List<String>> customListException(CustomListException e){
        return e.getWebExchangeBindExceptionMono()
                .flatMap(web -> Mono.just(web.getFieldErrors()))
                .flatMapMany(Flux::fromIterable)
                .map(error -> "El campo " + error.getField() + ", " + error.getDefaultMessage())
                .collectList();
    }

    @ExceptionHandler(TokenError.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Mono<String> tokenError(TokenError e){
        return Mono.just(e.getMensaje());
    }
}
