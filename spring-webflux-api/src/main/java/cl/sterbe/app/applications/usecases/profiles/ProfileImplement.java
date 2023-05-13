package cl.sterbe.app.applications.usecases.profiles;

import cl.sterbe.app.domains.models.profiles.Profile;
import cl.sterbe.app.domains.ports.in.profiles.ProfileUseCase;
import cl.sterbe.app.domains.ports.out.profiles.ProfileRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProfileImplement implements ProfileUseCase {

    @Autowired
    private ProfileRepositoryPort profileRepositoryPort;

    @Override
    public Flux<Profile> findAll() {
        return this.profileRepositoryPort.findAll();
    }

    @Override
    public Mono<Profile> findById(String id) {
        return this.profileRepositoryPort.findById(id);
    }

    @Override
    public Mono<Profile> save(Profile profile) {
        return this.profileRepositoryPort.save(profile);
    }

    @Override
    public Mono<Void> delete(Profile profile) {
        return this.profileRepositoryPort.delete(profile);
    }
}
