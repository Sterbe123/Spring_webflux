package cl.sterbe.app.domains.models.profiles;

import cl.sterbe.app.domains.models.friends.Friend;
import cl.sterbe.app.domains.models.multimedias.Photo;
import cl.sterbe.app.domains.models.multimedias.Video;
import cl.sterbe.app.domains.models.publications.Publication;
import cl.sterbe.app.domains.models.users.User;
import cl.sterbe.app.infrastructure.documents.friends.FriendDocument;
import cl.sterbe.app.infrastructure.documents.multimedias.PhotoDocument;
import cl.sterbe.app.infrastructure.documents.multimedias.VideoDocument;
import cl.sterbe.app.infrastructure.documents.profiles.ProfileDocument;
import cl.sterbe.app.infrastructure.documents.publications.PublicationDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Profile {

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

    public Mono<ProfileDocument> toDomainModel(){
        return this.user.toDomainModel().flatMap(userDocument -> {
            List<PhotoDocument> photoDocuments = new ArrayList<>();
            this.photos.forEach(p -> photoDocuments.add(p.toDomainModel()));

            List<VideoDocument> videoDocuments = new ArrayList<>();
            this.videos.forEach(v -> videoDocuments.add(v.toDomainModel()));

            List<FriendDocument> friendDocuments = new ArrayList<>();
            this.friends.forEach(f -> friendDocuments.add(f.toDomainModel()));

            List<Mono<PublicationDocument>> publicationMonos = new ArrayList<>();
            this.publications.forEach(p -> publicationMonos.add(p.toDomainModel()));

            return Flux.merge(publicationMonos).collectList()
                    .map(publicationDocuments -> new ProfileDocument(this.id, this.firstName, this.lastName,
                            this.contactInformation, userDocument, photoDocuments, videoDocuments,
                            friendDocuments, publicationDocuments, this.lastConnection));
        });
    }
}
