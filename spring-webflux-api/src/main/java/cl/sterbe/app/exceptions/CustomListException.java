package cl.sterbe.app.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

@Getter
@Setter
public class CustomListException extends Exception{

    private Mono<WebExchangeBindException> webExchangeBindExceptionMono;

    public CustomListException(Mono<WebExchangeBindException> webExchangeBindExceptionMono) {
        this.webExchangeBindExceptionMono = webExchangeBindExceptionMono;
    }
}
