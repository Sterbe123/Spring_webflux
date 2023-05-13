package cl.sterbe.app.domains.ports.out.profiles;

import cl.sterbe.app.domains.models.profiles.Profile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProfileRepositoryPort {

    Flux<Profile> findAll();

    Mono<Profile> findById(String id);

    Mono<Profile> save(Profile profile);

    Mono<Void> delete(Profile profile);
}
