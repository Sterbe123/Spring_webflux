package cl.sterbe.app.exceptions;

import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Mono;

@Getter
@Setter
public class CustomException extends Exception {

    public static Mono<Exception> castException(Throwable throwable){
         return Mono.just(new Exception())
                 .flatMap(exception -> {
                     if (throwable instanceof BadCredentialsException){
                         return Mono.just(new BadCredentialsException());
                     }

                     if(throwable instanceof NotUpdateResourceException){
                         return Mono.just(new NotUpdateResourceException());
                     }

                     if(throwable instanceof EmailAlreadyExistsException){
                         return Mono.just(new EmailAlreadyExistsException());
                     }

                     if(throwable instanceof  SamePasswordsException){
                         return Mono.just(new SamePasswordsException());
                     }

                     if(throwable instanceof TokenErrorException){
                         return Mono.just(new TokenErrorException(throwable.getMessage()));
                     }

                     if(throwable instanceof UserNotFoundException){
                         return Mono.just(new UserNotFoundException());
                     }

                     return Mono.just(exception);
                 });
    }
}
