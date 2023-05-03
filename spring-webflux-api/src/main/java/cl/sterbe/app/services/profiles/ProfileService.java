package cl.sterbe.app.services.profiles;

import cl.sterbe.app.documents.models.profiles.Profile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProfileService {

    Flux<Profile> findAll();

    Mono<Profile> findById(String id);

    Mono<Profile> save(Profile profile);

    Mono<Void> delete(Profile profile);
}
