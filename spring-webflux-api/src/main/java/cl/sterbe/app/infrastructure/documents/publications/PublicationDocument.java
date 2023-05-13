package cl.sterbe.app.infrastructure.documents.publications;

import cl.sterbe.app.domains.models.interactions.Comment;
import cl.sterbe.app.domains.models.interactions.Like;
import cl.sterbe.app.domains.models.multimedias.Photo;
import cl.sterbe.app.domains.models.multimedias.Video;
import cl.sterbe.app.domains.models.publications.Publication;
import cl.sterbe.app.infrastructure.documents.multimedias.PhotoDocument;
import cl.sterbe.app.infrastructure.documents.multimedias.VideoDocument;
import cl.sterbe.app.infrastructure.documents.profiles.ProfileDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Document(collection = "publications")
public class PublicationDocument {

    @Id
    private String id;

    private String name;

    private List<PhotoDocument> photos;

    private List<VideoDocument> videos;

    private List<Comment> comments;

    private List<Like> likes;

    private ProfileDocument profile;

    private boolean status;

    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    private Date createAt;

    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    private Date updateAt;

    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    private Date deleteAt;

    public Mono<Publication> toDomainModel(){
        return this.profile.toDomainModel()
                .flatMap(profile -> {
                    List<Photo> photosModels = new ArrayList<>();
                    this.photos.forEach(p -> photosModels.add(p.toDomainModel()));

                    List<Video> videoModels = new ArrayList<>();
                    this.videos.forEach(v -> videoModels.add(v.toDomainModel()));

                    return Mono.just(new Publication(this.id, this.name, photosModels, videoModels,
                            this.comments, this.likes, profile, this.status, this.createAt, this.updateAt,
                            this.deleteAt));
                });
    }
}
