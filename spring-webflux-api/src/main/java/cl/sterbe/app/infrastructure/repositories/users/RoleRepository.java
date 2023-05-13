package cl.sterbe.app.infrastructure.repositories.users;

import cl.sterbe.app.infrastructure.documents.users.RoleDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface RoleRepository extends ReactiveMongoRepository<RoleDocument, String> {

    Mono<RoleDocument> findOneByName(String name);
}
