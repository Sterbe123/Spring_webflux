package cl.sterbe.app.services.users;

import cl.sterbe.app.documents.dto.email.EmailMapper;
import cl.sterbe.app.documents.models.users.User;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface UserService {

    Flux<User> findAll();

    Mono<User> findById(String id);

    Mono<User> save(User user);

    Mono<Void> delete(String id);

    Mono<User> findOneByEmail(String email);

    Mono<User> login(EmailMapper emailMapper, ServerWebExchange serverWebExchange);

    Mono<Map<String,Object>> register(EmailMapper emailMapper);
}
