package cl.sterbe.app.documents.dao.users;

import cl.sterbe.app.documents.models.users.Role;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface RoleDAO extends ReactiveMongoRepository<Role, String> {

    Mono<Role> findOneByRole(String role);
}
