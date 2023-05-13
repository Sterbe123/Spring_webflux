package cl.sterbe.app.domains.ports.out.users;

import cl.sterbe.app.domains.models.users.Role;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoleRepositoryPort {

    Flux<Role> findAll();

    Mono<Role> findById(String id);

    Mono<Role> save(Role role);

    Mono<Void> delete(String id);

    Mono<Role> findOneByName(String name);
}
