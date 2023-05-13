package cl.sterbe.app.infrastructure.documents.friends;

import cl.sterbe.app.domains.models.friends.Friend;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@Document(collection = "friends")
public class FriendDocument {

    @Id
    private String id;

    private String idProfile;

    public Friend toDomainModel(){
        return new Friend(this.id, this.idProfile);
    }

    public FriendDocument fromDomainModel(Friend friend){
        return new FriendDocument(friend.getId(), friend.getIdProfile());
    }
}
