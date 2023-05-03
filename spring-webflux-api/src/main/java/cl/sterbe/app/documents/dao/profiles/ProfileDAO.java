package cl.sterbe.app.documents.dao.profiles;

import cl.sterbe.app.documents.models.profiles.Profile;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProfileDAO extends ReactiveMongoRepository<Profile, String> {
}
