package cl.sterbe.app.domains.exceptions;

import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Mono;

@Getter
@Setter
public class WebExchangeException extends Exception{

    private Mono<Throwable> throwable;

    public WebExchangeException(Mono<Throwable> throwable) {
        this.throwable = throwable;
    }
}
