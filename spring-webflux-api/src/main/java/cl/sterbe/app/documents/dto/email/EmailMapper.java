package cl.sterbe.app.documents.dto.email;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmailMapper {

    @Email
    @NotEmpty
    @NotNull
    private String email;

    @NotNull
    @NotEmpty
    private String password;
}
