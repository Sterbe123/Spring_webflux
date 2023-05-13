package cl.sterbe.app.domains.models.users;

import cl.sterbe.app.infrastructure.documents.users.RoleDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import reactor.core.publisher.Mono;

@Getter
@Setter
@AllArgsConstructor
public class Role {

    private String id;

    private String name;

    public RoleDocument toDomainModel(){
        return  new RoleDocument(this.id, this.name);
    }
}
