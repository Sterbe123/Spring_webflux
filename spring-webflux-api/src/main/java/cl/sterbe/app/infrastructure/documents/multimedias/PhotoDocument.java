package cl.sterbe.app.infrastructure.documents.multimedias;

import cl.sterbe.app.domains.models.interactions.Comment;
import cl.sterbe.app.domains.models.interactions.Like;
import cl.sterbe.app.domains.models.multimedias.Photo;
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
@Document(collection = "photos")
public class PhotoDocument {

    @Id
    private String id;

    private String name;

    private List<Comment> comments;

    private List<Like> likes;

    private boolean profilePicture;

    private boolean coverPhoto;

    private boolean status;

    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    private Date createAt;

    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    private Date deleteAt;

    public Photo toDomainModel(){
        return new Photo(this.id, this.name, this.comments, this.likes, this.profilePicture, this.coverPhoto,
                this.status, this.createAt, this.deleteAt);
    }
}
