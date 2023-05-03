package cl.sterbe.app.documents.models.friends;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@Document(collection = "friends")
public class Friend {

    @Id
    private String id;

    private String idProfile;
}
