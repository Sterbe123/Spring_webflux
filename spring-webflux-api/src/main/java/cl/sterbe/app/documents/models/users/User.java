package cl.sterbe.app.documents.models.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String id;

    @Email
    @NotEmpty
    @NotNull
    @UniqueElements
    private String email;

    @NotNull
    @NotEmpty
    private String password;

    private List<Role> roles;

    private boolean status;

    private boolean verified;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date createAt;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date updateAt;
}