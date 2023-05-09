package cl.sterbe.app.services.users.implement;

import cl.sterbe.app.componets.security.TokenUtils;
import cl.sterbe.app.documents.dao.users.RoleDAO;
import cl.sterbe.app.documents.dao.users.UserDAO;
import cl.sterbe.app.documents.dto.email.EmailMapper;
import cl.sterbe.app.documents.models.users.User;
import cl.sterbe.app.exceptions.BadCredentialsException;
import cl.sterbe.app.exceptions.EmailAlreadyExistsException;
import cl.sterbe.app.exceptions.SamePasswordsException;
import cl.sterbe.app.exceptions.UserNotFoundException;
import cl.sterbe.app.services.users.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class UserImplement implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Logger logger = LoggerFactory.getLogger(UserImplement.class);

    @Override
    public Flux<User> findAll() {
        return this.userDAO.findAll();
    }

    @Override
    public Mono<User> findById(String id) {
        return this.userDAO.findById(id)
                .switchIfEmpty(Mono.error(new UserNotFoundException()));
    }

    @Override
    public Mono<User> save(User user) {
        return this.userDAO.save(user);
    }

    @Override
    public Mono<Void> delete(String id) {
        return this.userDAO.findById(id)
                .flatMap(user -> this.userDAO.delete(user))
                .switchIfEmpty(Mono.error(new UserNotFoundException()));
    }

    @Override
    public Mono<User> findOneByEmail(String email) {
        return this.userDAO.findOneByEmail(email)
                .switchIfEmpty(Mono.error(new UserNotFoundException()));
    }

    @Override
    public Mono<User> login(EmailMapper emailMapper, ServerWebExchange serverWebExchange) {
        return this.userDAO.findOneByEmail(emailMapper.getEmail())
                .filter(user -> this.passwordEncoder.matches(emailMapper.getPassword(), user.getPassword()))
                .map(user -> {
                    serverWebExchange
                            .getResponse()
                            .getHeaders()
                            .add(HttpHeaders.AUTHORIZATION, TokenUtils.generateToken(new cl.sterbe.app.documents
                                    .models.oauth.User(user.getEmail()
                                    ,null,user.getRoles())).block());
                    return user;
                })
                .switchIfEmpty(Mono.error(new BadCredentialsException()));
    }

    @Override
    public Mono<Map<String,Object>> register(EmailMapper emailMapper) {

        return this.userDAO.findOneByEmail(emailMapper.getEmail()).hasElement()
                .filter(exists -> !exists)
                .flatMap(user -> this.roleDAO.findOneByName("ROLE_USER")
                        .flatMap(role ->
                             this.userDAO.save(new User(
                                    null,
                                    emailMapper.getEmail(),
                                    this.passwordEncoder.encode(emailMapper.getPassword()),
                                    Collections.singletonList(role),
                                    true,false, new Date(), null))
                                    .map(u -> Map.of("user", u,
                                            "token", Objects.requireNonNull(TokenUtils.generateToken
                                                    (new cl.sterbe.app.documents.models.oauth.User(u.getEmail(),
                                                            null, u.getRoles())).block())))))
                .switchIfEmpty(Mono.error(new EmailAlreadyExistsException()));

    /*    return this.userDAO.findOneByEmail(emailMapper.getEmail()).hasElement()
                .flatMap(exists -> exists?Mono.error(new EmailAlreadyExistsException())
                        : this.roleDAO.findOneByName("ROLE_USER")
                                .flatMap(role -> this.userDAO.save(new User(
                                        null,
                                        emailMapper.getEmail(),
                                        this.passwordEncoder.encode(emailMapper.getPassword()),
                                        Collections.singletonList(role),
                                        true,
                                        false,
                                        new Date(),
                                        null
                                )))
                        .map(user -> Map.of("user",user,
                                "token", Objects.requireNonNull(TokenUtils.generateToken(new cl.sterbe.app.documents
                                        .models.oauth.User(user.getEmail()
                                        , null, user.getRoles())).block()))));  */
    }

    @Override
    public Mono<User> updatePassword(EmailMapper emailMapper) {
        return this.userDAO.findOneByEmail(emailMapper.getEmail())
                .flatMap(user -> this.passwordEncoder.matches(emailMapper.getPassword(), user.getPassword())
                            ?Mono.error(new SamePasswordsException())
                            :Mono.just(user))
                .map(user -> {
                    user.setPassword(this.passwordEncoder.encode(emailMapper.getPassword()));
                    return user;
                })
                .switchIfEmpty(Mono.error(new BadCredentialsException()));
    }
}
