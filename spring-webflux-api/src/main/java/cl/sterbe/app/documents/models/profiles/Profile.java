package cl.sterbe.app.documents.models.profiles;

import cl.sterbe.app.documents.dto.profiles.ContactInformation;
import cl.sterbe.app.documents.models.friends.Friend;
import cl.sterbe.app.documents.models.multimedias.Photo;
import cl.sterbe.app.documents.models.multimedias.Video;
import cl.sterbe.app.documents.models.publications.Publication;
import cl.sterbe.app.documents.models.users.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Document(collection = "profiles")
public class Profile {

    @Id
    private String id;

    private String firstName;

    private String lastName;

    private ContactInformation contactInformation;

    private User user;

    private List<Photo> photos;

    private List<Video> videos;

    private List<Friend> friends;

    private List<Publication> publications;

    @DateTimeFormat(pattern = "dd/MM/yyyy hh:ss:mm")
    private Date lastConnection;
}
