package cl.sterbe.app.advices;

import cl.sterbe.app.exceptions.CustomException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;
import java.util.Map;

@RestControllerAdvice
public class HandlerException {

    @ExceptionHandler(CustomException.class)
    public Mono<ResponseEntity<Map<String,String>>> customException(CustomException e){
        return Mono.just(ResponseEntity
                .status(e.getHttpStatus())
                        .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("error", e.getMessage())));
    }
}
