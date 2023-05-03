package cl.sterbe.app.services.profiles.implement;

import cl.sterbe.app.documents.dao.profiles.ProfileDAO;
import cl.sterbe.app.documents.models.profiles.Profile;
import cl.sterbe.app.services.profiles.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProfileImplement implements ProfileService {

    @Autowired
    private ProfileDAO profileDAO;

    @Override
    public Flux<Profile> findAll() {
        return this.profileDAO.findAll();
    }

    @Override
    public Mono<Profile> findById(String id) {
        return this.profileDAO.findById(id);
    }

    @Override
    public Mono<Profile> save(Profile profile) {
        return this.profileDAO.save(profile);
    }

    @Override
    public Mono<Void> delete(Profile profile) {
        return this.profileDAO.delete(profile);
    }
}
