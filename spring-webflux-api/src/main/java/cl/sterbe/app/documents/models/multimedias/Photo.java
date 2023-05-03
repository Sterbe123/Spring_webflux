package cl.sterbe.app.documents.models.multimedias;

import cl.sterbe.app.documents.entity.interactions.Comment;
import cl.sterbe.app.documents.entity.interactions.Like;
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
public class Photo {

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
}
