package cl.sterbe.app.domains.models.users;

import cl.sterbe.app.infrastructure.documents.users.RoleDocument;
import cl.sterbe.app.infrastructure.documents.users.UserDocument;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class User {

    private String id;

    @Email
    @NotEmpty
    @NotNull
    private String email;

    @NotNull
    @NotEmpty
    @JsonIgnore
    private String password;

    private List<Role> roles;

    private boolean status;

    private boolean verified;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date createAt;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date updateAt;

    public Mono<UserDocument> toDomainModel(){
        return Mono.fromCallable(() -> {
            List<Mono<RoleDocument>> monoRoleDocuments = new ArrayList<>();
            this.roles.forEach(r -> monoRoleDocuments.add(r.toDomainModel()));

            return Flux.merge(monoRoleDocuments).collectList()
                    .map(roleDocuments -> new UserDocument(this.getId(), this.email, this.password,
                            roleDocuments, this.status, this.verified, this.createAt, this.updateAt));
        }).flatMap(user -> user.flatMap(Mono::just));
    }
}
