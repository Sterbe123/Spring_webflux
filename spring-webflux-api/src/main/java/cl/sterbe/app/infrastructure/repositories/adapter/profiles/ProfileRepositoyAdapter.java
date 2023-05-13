package cl.sterbe.app.infrastructure.repositories.adapter.profiles;

import cl.sterbe.app.domains.models.profiles.Profile;
import cl.sterbe.app.domains.ports.out.profiles.ProfileRepositoryPort;
import cl.sterbe.app.infrastructure.documents.profiles.ProfileDocument;
import cl.sterbe.app.infrastructure.repositories.profiles.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProfileRepositoyAdapter implements ProfileRepositoryPort {

    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public Flux<Profile> findAll() {
        return this.profileRepository.findAll()
                .flatMap(ProfileDocument::toDomainModel);
    }

    @Override
    public Mono<Profile> findById(String id) {
        return this.profileRepository.findById(id)
                .flatMap(ProfileDocument::toDomainModel);
    }

    @Override
    public Mono<Profile> save(Profile profile) {
        return profile.toDomainModel()
                .flatMap(p -> this.profileRepository.save(p))
                .flatMap(ProfileDocument::toDomainModel);
    }

    @Override
    public Mono<Void> delete(Profile profile) {
        return profile.toDomainModel()
                .flatMap(p -> this.profileRepository.delete(p));
    }
}
