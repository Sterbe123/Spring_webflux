package cl.sterbe.app.componets.security;

import cl.sterbe.app.exceptions.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class FilterManager implements WebFilter {

    @Autowired
    private TokenUtils tokenUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();

        if(path.contains("auth")){
            return chain.filter(exchange);
        }

        String auth = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if(auth == null){
            return Mono.error(new CustomException("no token was found", HttpStatus.NOT_FOUND));
        }


        if(!auth.startsWith("Bearer ")){
            return Mono.error(new CustomException("invalid auth", HttpStatus.BAD_REQUEST));
        }

        String token = auth.replace("Bearer ","");
        exchange.getAttributes().put("token",token);
        return chain.filter(exchange);
    }
}
