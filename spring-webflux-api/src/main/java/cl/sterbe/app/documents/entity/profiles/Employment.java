package cl.sterbe.app.documents.entity.profiles;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Employment {

    private String company;

    private String job;

    private String town;

    private String description;

    private boolean status;
}
