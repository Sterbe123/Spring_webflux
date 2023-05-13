package cl.sterbe.app.infrastructure.components;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class FilterManager implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return Mono.just(exchange)
                .flatMap(e -> {
                    String path = e.getRequest().getPath().value();
                    if(path.contains("auth")){
                        return Mono.just(true);
                    }

                    String auth = e.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

                    if(auth==null){
                         return Mono.just(false);
                      //  throw new TokenErrorException("token not found");
                    }

                    if(!auth.startsWith("Bearer ")){
                        return Mono.just(false);
                      //  throw new TokenErrorException("invalid token");
                    }

                    String token = auth.replace("Bearer ","");
                    exchange.getAttributes().put("token",token);
                    return Mono.just(true);
                })
                .flatMap(pass -> pass?chain.filter(exchange):Mono.empty());
    }
}
