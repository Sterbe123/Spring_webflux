package cl.sterbe.app.domains.ports.out.users;

import cl.sterbe.app.domains.models.users.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface UserRepositoryPort {

    Flux<User> findAll();

    Mono<User> findById(String id);

    Mono<User> save(User user);

    Mono<Void> delete(User user);

    Mono<User> findOneByEmail(String email);
}
