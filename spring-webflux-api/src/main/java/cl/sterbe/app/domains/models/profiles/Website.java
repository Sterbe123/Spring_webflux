package cl.sterbe.app.domains.models.profiles;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Website {

    private String url;

    private boolean status;
}
