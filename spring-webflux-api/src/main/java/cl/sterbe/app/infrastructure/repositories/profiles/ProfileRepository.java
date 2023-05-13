package cl.sterbe.app.infrastructure.repositories.profiles;

import cl.sterbe.app.infrastructure.documents.profiles.ProfileDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProfileRepository extends ReactiveMongoRepository<ProfileDocument, String> {
}
