package cl.sterbe.app.infrastructure.documents.profiles;

import cl.sterbe.app.domains.models.friends.Friend;
import cl.sterbe.app.domains.models.multimedias.Photo;
import cl.sterbe.app.domains.models.multimedias.Video;
import cl.sterbe.app.domains.models.profiles.ContactInformation;
import cl.sterbe.app.domains.models.profiles.Profile;
import cl.sterbe.app.domains.models.publications.Publication;
import cl.sterbe.app.infrastructure.documents.friends.FriendDocument;
import cl.sterbe.app.infrastructure.documents.multimedias.PhotoDocument;
import cl.sterbe.app.infrastructure.documents.multimedias.VideoDocument;
import cl.sterbe.app.infrastructure.documents.publications.PublicationDocument;
import cl.sterbe.app.infrastructure.documents.users.UserDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Document(collection = "profiles")
public class ProfileDocument {

    @Id
    private String id;

    private String firstName;

    private String lastName;

    private ContactInformation contactInformation;

    private UserDocument user;

    private List<PhotoDocument> photos;

    private List<VideoDocument> videos;

    private List<FriendDocument> friends;

    private List<PublicationDocument> publications;

    @DateTimeFormat(pattern = "dd/MM/yyyy hh:ss:mm")
    private Date lastConnection;

    public Mono<Profile> toDomainModel(){
        return this.user.toDomainModel().flatMap(user -> {
            List<Photo> photoModels = new ArrayList<>();
            List<Video> videoModels = new ArrayList<>();
            List<Friend> friendsModels = new ArrayList<>();
            List<Mono<Publication>> publicationMonos = new ArrayList<>();

            this.photos.forEach(p -> photoModels.add(p.toDomainModel()));
            this.videos.forEach(v -> videoModels.add(v.toDomainModel()));
            this.friends.forEach(f -> friendsModels.add(f.toDomainModel()));
            this.publications.forEach(p -> publicationMonos.add(p.toDomainModel()));

            return Flux.merge(publicationMonos).collectList()
                    .map(publicationsModels -> new Profile(this.id, this.firstName, this.lastName, this.contactInformation,
                                user, photoModels, videoModels, friendsModels, publicationsModels, this.lastConnection));
        });
    }
}
