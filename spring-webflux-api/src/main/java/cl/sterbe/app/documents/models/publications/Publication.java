package cl.sterbe.app.documents.models.publications;

import cl.sterbe.app.documents.entity.interactions.Comment;
import cl.sterbe.app.documents.entity.interactions.Like;
import cl.sterbe.app.documents.models.multimedias.Photo;
import cl.sterbe.app.documents.models.multimedias.Video;
import cl.sterbe.app.documents.models.profiles.Profile;
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
@Document(collection = "publications")
public class Publication {

    @Id
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
}
