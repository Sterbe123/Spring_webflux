package cl.sterbe.app.documents.entity.interactions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Like {

    private String name;

    private boolean normal;

    private boolean iLike;

    private boolean itMettersToMe;

    private boolean iLove;

    private boolean itSaddensMe;

    private boolean itInfuriatesMe;
}
