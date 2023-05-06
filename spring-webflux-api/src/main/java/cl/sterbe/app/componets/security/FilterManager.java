package cl.sterbe.app.componets.security;

import cl.sterbe.app.exceptions.CustomException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class FilterManager implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return Mono.fromCallable(() -> {
            String path = exchange.getRequest().getPath().value();

            if(path.contains("auth")){
                return true;
            }

            String auth = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if(auth == null){
                throw new CustomException("no token was found", HttpStatus.NOT_FOUND);
            }

            if(!auth.startsWith("Bearer ")){
                throw new CustomException("invalid auth", HttpStatus.BAD_REQUEST);
            }

            String token = auth.replace("Bearer ","");
            exchange.getAttributes().put("token",token);
            return true;
        }).flatMap(pass -> {
            if(pass){
                return chain.filter(exchange);
            }
            return Mono.empty();
        });
    }
}
