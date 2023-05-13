package cl.sterbe.app.domains.models.profiles;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class Home {

    private String city;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date transferDate;

    private boolean status;
}
