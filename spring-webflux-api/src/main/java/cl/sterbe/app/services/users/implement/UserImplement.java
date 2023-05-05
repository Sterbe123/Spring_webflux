package cl.sterbe.app.services.users.implement;

import cl.sterbe.app.componets.security.TokenUtils;
import cl.sterbe.app.documents.dao.users.RoleDAO;
import cl.sterbe.app.documents.dao.users.UserDAO;
import cl.sterbe.app.documents.dto.email.EmailMapper;
import cl.sterbe.app.documents.models.users.Role;
import cl.sterbe.app.documents.models.users.User;
import cl.sterbe.app.exceptions.CustomException;
import cl.sterbe.app.services.users.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Date;

@Service
public class UserImplement implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenUtils tokenUtils;

    private Logger logger = LoggerFactory.getLogger(UserImplement.class);

    @Override
    public Flux<User> findAll() {
        return this.userDAO.findAll();
    }

    @Override
    public Mono<User> findById(String id) {
        return this.userDAO.findById(id)
                .switchIfEmpty(Mono.error(new CustomException("user not found", HttpStatus.NOT_FOUND)));
    }

    @Override
    public Mono<User> save(User user) {
        return this.userDAO.save(user);
    }

    @Override
    public Mono<Void> delete(User user) {
        return this.userDAO.delete(user);
    }

    @Override
    public Mono<User> findOneByEmail(String email) {
        return this.userDAO.findOneByEmail(email)
                .switchIfEmpty(Mono.error(new CustomException("user not found", HttpStatus.NOT_FOUND)));
    }

    @Override
    public Mono<User> login(EmailMapper emailMapper, ServerWebExchange serverWebExchange) {
        return this.userDAO.findOneByEmail(emailMapper.getEmail())
                .filter(user -> this.passwordEncoder.matches(emailMapper.getPassword(), user.getPassword()))
                .map(user -> {
                    serverWebExchange
                            .getResponse()
                            .getHeaders()
                            .add(HttpHeaders.AUTHORIZATION, this.tokenUtils.generateToken(new cl.sterbe.app.documents.models.oauth.User(user.getEmail()
                                    ,user.getPassword(),user.getRoles())));
                    return user;
                })
                .switchIfEmpty(Mono.error(new CustomException("bad credentials", HttpStatus.BAD_REQUEST)));
    }

    //TODO: arreglar esto.
    @Override
    public Mono<User> register(User user) {
        Role role = this.roleDAO.findOneByRole("ROLE_USER").block();
        logger.info(role.getRole());
        user.setStatus(true);
        user.setVerified(false);
        user.setCreateAt(new Date());
        user.setRoles(Arrays.asList(role));
        Mono<Boolean> userExists = this.userDAO.findOneByEmail(user.getEmail()).hasElement();
        return userExists
                .flatMap(exists -> exists?
                        Mono.error(new CustomException("email already in use", HttpStatus.INTERNAL_SERVER_ERROR))
                        : this.userDAO.save(user));
    }
}
