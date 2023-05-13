package cl.sterbe.app.infrastructure.repositories.adapter.users;

import cl.sterbe.app.domains.models.users.Role;
import cl.sterbe.app.domains.ports.out.users.RoleRepositoryPort;
import cl.sterbe.app.infrastructure.documents.users.RoleDocument;
import cl.sterbe.app.infrastructure.repositories.users.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RoleRepositoryAdapter implements RoleRepositoryPort {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Flux<Role> findAll() {
        return this.roleRepository.findAll()
                .flatMap(RoleDocument::toDomainModel);
    }

    @Override
    public Mono<Role> findById(String id) {
        return this.roleRepository.findById(id)
                .flatMap(RoleDocument::toDomainModel);
    }

    @Override
    public Mono<Role> save(Role role) {
        return role.toDomainModel()
                .flatMap(rol -> this.roleRepository.save(rol))
                .flatMap(RoleDocument::toDomainModel);
    }

    @Override
    public Mono<Void> delete(String id) {
        return this.roleRepository.deleteById(id);
    }

    @Override
    public Mono<Role> findOneByName(String name) {
        return this.roleRepository.findOneByName(name)
                .flatMap(RoleDocument::toDomainModel);
    }
}
