package cl.sterbe.app.infrastructure.repositories.adapter.users;

import cl.sterbe.app.domains.models.users.User;
import cl.sterbe.app.domains.ports.out.users.UserRepositoryPort;
import cl.sterbe.app.infrastructure.documents.users.UserDocument;
import cl.sterbe.app.infrastructure.repositories.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserRepositoryAdapter implements UserRepositoryPort {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Flux<User> findAll() {
        return this.userRepository.findAll()
                .flatMap(UserDocument::toDomainModel);
    }

    @Override
    public Mono<User> findById(String id) {
        return this.userRepository.findById(id)
                .flatMap(UserDocument::toDomainModel);
    }

    @Override
    public Mono<User> save(User user) {
        return user.toDomainModel()
                .flatMap(u -> this.userRepository.save(u)
                        .flatMap(UserDocument::toDomainModel));
    }

    @Override
    public Mono<Void> delete(User user) {
        return user.toDomainModel()
                .flatMap(u -> this.userRepository.delete(u));
    }

    @Override
    public Mono<User> findOneByEmail(String email) {
        return this.userRepository.findOneByEmail(email)
                .flatMap(UserDocument::toDomainModel);
    }
}
