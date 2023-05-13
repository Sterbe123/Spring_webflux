package cl.sterbe.app.infrastructure.documents.users;

import cl.sterbe.app.domains.models.users.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "roles")
@AllArgsConstructor
public class RoleDocument {

    @Id
    private String id;

    private String name;

    public Role toDomainModel(){
        return new Role(this.id, this.name);
    }
}
