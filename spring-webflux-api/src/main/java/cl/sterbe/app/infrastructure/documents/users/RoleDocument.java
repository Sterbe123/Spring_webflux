package cl.sterbe.app.infrastructure.documents.users;

import cl.sterbe.app.domains.models.users.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import reactor.core.publisher.Mono;

@Getter
@Setter
@Document(collection = "roles")
@AllArgsConstructor
public class RoleDocument {

    @Id
    private String id;

    private String name;

    public Mono<Role> toDomainModel(){
        return Mono.just(new Role(this.id, this.name));
    }
}
