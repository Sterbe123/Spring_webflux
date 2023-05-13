package cl.sterbe.app.domains.models.friends;

import cl.sterbe.app.infrastructure.documents.friends.FriendDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Friend {

    private String id;

    private String idProfile;

    public FriendDocument toDomainModel(){
        return new FriendDocument(this.getId(), this.getIdProfile());
    }
}
