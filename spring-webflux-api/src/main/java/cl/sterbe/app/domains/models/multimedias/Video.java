package cl.sterbe.app.domains.models.multimedias;

import cl.sterbe.app.domains.models.interactions.Comment;
import cl.sterbe.app.domains.models.interactions.Like;
import cl.sterbe.app.infrastructure.documents.multimedias.VideoDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Video {

    private String id;

    private String name;

    private List<Comment> comments;

    private List<Like> likes;

    private boolean status;

    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    private Date createAt;

    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    private Date deleteAt;

    public VideoDocument toDomainModel(){
        return new VideoDocument(this.id, this.name, this.getComments(), this.getLikes(), this.isStatus(),
                this.getCreateAt(), this.getDeleteAt());
    }
}
