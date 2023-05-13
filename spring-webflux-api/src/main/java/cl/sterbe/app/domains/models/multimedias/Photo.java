package cl.sterbe.app.domains.models.multimedias;

import cl.sterbe.app.domains.models.interactions.Comment;
import cl.sterbe.app.domains.models.interactions.Like;
import cl.sterbe.app.infrastructure.documents.multimedias.PhotoDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Photo {

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

    public PhotoDocument toDomainModel(){
        return new PhotoDocument(this.id, this.name, this.getComments(), this.getLikes(), this.isProfilePicture(),
                this.isCoverPhoto(), this.isStatus(), this.getCreateAt(), this.getDeleteAt());
    }
}
