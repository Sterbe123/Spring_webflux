package cl.sterbe.app.documents.entity.profiles;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ContactInformation {

    private String phone;

    private String email;

    private List<Home> homes;

    private List<Employment> employments;

    private List<Website> websites;

    private List<Lenguage> lenguages;

    private String gender;

    private Date birthdate;
}
