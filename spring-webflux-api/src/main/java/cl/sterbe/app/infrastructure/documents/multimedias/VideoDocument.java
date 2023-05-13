package cl.sterbe.app.infrastructure.documents.multimedias;

import cl.sterbe.app.domains.models.interactions.Comment;
import cl.sterbe.app.domains.models.interactions.Like;
import cl.sterbe.app.domains.models.multimedias.Video;
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
@Document(collection = "videos")
public class VideoDocument {

    @Id
    private String id;

    private String name;

    private List<Comment> comments;

    private List<Like> likes;

    private boolean status;

    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    private Date createAt;

    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    private Date deleteAt;

    public Video toDomainModel(){
        return new Video(this.id, this.name, this.comments, this.likes, this.status, this.createAt, this.deleteAt);
    }

    private VideoDocument fromDomainModel(Video video){
        return new VideoDocument(video.getId(), video.getName(), video.getComments(), video.getLikes(), video.isStatus(),
                video.getCreateAt(), video.getDeleteAt());
    }
}
