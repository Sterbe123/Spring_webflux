package cl.sterbe.app.infrastructure.repositories.users;

import cl.sterbe.app.infrastructure.documents.users.UserDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<UserDocument, String> {

    Mono<UserDocument> findOneByEmail(String email);
}
