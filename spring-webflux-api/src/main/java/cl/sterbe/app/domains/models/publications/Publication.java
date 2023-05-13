package cl.sterbe.app.domains.models.publications;

import cl.sterbe.app.domains.models.interactions.Comment;
import cl.sterbe.app.domains.models.interactions.Like;
import cl.sterbe.app.domains.models.multimedias.Photo;
import cl.sterbe.app.domains.models.multimedias.Video;
import cl.sterbe.app.domains.models.profiles.Profile;
import cl.sterbe.app.infrastructure.documents.multimedias.PhotoDocument;
import cl.sterbe.app.infrastructure.documents.multimedias.VideoDocument;
import cl.sterbe.app.infrastructure.documents.publications.PublicationDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Publication {

    private String id;

    private String name;

    private List<Photo> photos;

    private List<Video> videos;

    private List<Comment> comments;

    private List<Like> likes;

    private Profile profile;

    private boolean status;

    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    private Date createAt;

    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    private Date updateAt;

    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    private Date deleteAt;

    public Mono<PublicationDocument> toDomainModel(){
        return this.profile.toDomainModel()
                .flatMap(profileDocument -> {
                    List<PhotoDocument> photoDocuments = new ArrayList<>();
                    this.getPhotos().forEach(p -> photoDocuments.add(p.toDomainModel()));

                    List<VideoDocument> videoDocuments = new ArrayList<>();
                    this.getVideos().forEach(v -> videoDocuments.add(v.toDomainModel()));

                    return Mono.just(new PublicationDocument(this.id, this.name, photoDocuments, videoDocuments, this.getComments(), this.getLikes(),
                            profileDocument, this.status, this.createAt, this.updateAt, this.deleteAt));
                });
    }
}
