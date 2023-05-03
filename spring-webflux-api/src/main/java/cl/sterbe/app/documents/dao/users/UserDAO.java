package cl.sterbe.app.documents.dao.users;

import cl.sterbe.app.documents.models.users.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserDAO extends ReactiveMongoRepository<User, String> {
}
