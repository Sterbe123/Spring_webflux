package cl.sterbe.app.infrastructure.documents.users;

import cl.sterbe.app.domains.models.users.Role;
import cl.sterbe.app.domains.models.users.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Document(collection = "users")
public class UserDocument {

    @Id
    private String id;

    private String email;

    private String password;

    private List<RoleDocument> roles;

    private boolean status;

    private boolean verified;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date createAt;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date updateAt;

    public Mono<User> toDomainModel(){
        return Mono.fromCallable(() -> {
            List<Mono<Role>> monoRoleModels = new ArrayList<>();
            this.roles.forEach(r -> monoRoleModels.add(r.toDomainModel()));

            return Flux.merge(monoRoleModels).collectList()
                    .map(rolesModels ->  new User(this.id, this.getEmail(), this.password, rolesModels,
                            this.status, this.verified, this.createAt, this.updateAt));
        }).flatMap(user -> user.flatMap(Mono::just));
    }
}
