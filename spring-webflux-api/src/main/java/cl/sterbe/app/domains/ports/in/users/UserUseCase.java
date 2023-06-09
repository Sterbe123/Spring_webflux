package cl.sterbe.app.domains.ports.in.users;

import cl.sterbe.app.domains.models.email.EmailMapper;
import cl.sterbe.app.domains.models.users.User;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface UserUseCase {

    Flux<User> findAll();

    Mono<User> findById(String id);

    Mono<User> save(User user);

    Mono<Void> delete(String id);

    Mono<User> findOneByEmail(String email);

    Mono<User> login(EmailMapper emailMapper, ServerWebExchange serverWebExchange);

    Mono<Map<String,Object>> register(EmailMapper emailMapper);

    Mono<User> updatePassword(EmailMapper emailMapper);

    Mono<String> validateAccount(String token);

    Mono<Map<String,String>> reSendToken(String email);
}
