package cl.sterbe.app.documents.models.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "roles")
@AllArgsConstructor
public class Role {

    @Id
    private String id;

    private String role;
}
