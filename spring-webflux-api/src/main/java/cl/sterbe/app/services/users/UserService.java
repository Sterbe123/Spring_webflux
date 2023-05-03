package cl.sterbe.app.services.users;

import cl.sterbe.app.documents.models.users.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

    Flux<User> findAll();

    Mono<User> findById(String id);

    Mono<User> save(User user);

    Mono<Void> delete(User user);
}
