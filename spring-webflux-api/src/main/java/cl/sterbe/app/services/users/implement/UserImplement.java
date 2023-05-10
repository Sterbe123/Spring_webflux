package cl.sterbe.app.services.users.implement;

import cl.sterbe.app.componets.security.TokenUtils;
import cl.sterbe.app.documents.dao.users.RoleDAO;
import cl.sterbe.app.documents.dao.users.UserDAO;
import cl.sterbe.app.documents.dto.email.EmailMapper;
import cl.sterbe.app.documents.dto.oauth.UserAuth;
import cl.sterbe.app.documents.models.users.User;
import cl.sterbe.app.exceptions.*;
import cl.sterbe.app.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
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
                .flatMap(user -> user.isStatus()?Mono.just(user):Mono.error(new DisabledUserException()))
                .flatMap(user -> TokenUtils.generateToken(new UserAuth(user.getEmail(),null,user.getRoles()),
                                60)
                            .flatMap(token -> {
                                 serverWebExchange
                                        .getResponse()
                                        .getHeaders()
                                        .add(HttpHeaders.AUTHORIZATION, token);
                                 return Mono.just(user);
                            }))
                .switchIfEmpty(Mono.error(new BadCredentialsException()));
    }

    @Override
    public Mono<Map<String,Object>> register(EmailMapper emailMapper) {
        return this.userDAO.findOneByEmail(emailMapper.getEmail()).hasElement()
                .flatMap(exists -> exists?Mono.error(new EmailAlreadyExistsException())
                        :this.roleDAO.findOneByName("ROLE_USER")
                        .flatMap(role -> this.userDAO.save(new User(
                                        null,
                                        emailMapper.getEmail(),
                                        this.passwordEncoder.encode(emailMapper.getPassword()),
                                        Collections.singletonList(role), true,
                                        false, new Date(), null))
                                        .flatMap(user -> TokenUtils.generateToken(new UserAuth(
                                                    user.getEmail(), null, user.getRoles()), 5)
                                                    .map(token -> Map.of("user",
                                                            user, "token verification", token)))));
    }

    @Override
    public Mono<User> updatePassword(EmailMapper emailMapper) {
        return ReactiveSecurityContextHolder.getContext()
                .flatMap(reactiveSecurity -> !reactiveSecurity.getAuthentication().getName().equals(emailMapper.getEmail())
                        ?Mono.error(new NotUpdateResourceException())
                        :this.userDAO.findOneByEmail(emailMapper.getEmail()))
                .flatMap(user -> this.passwordEncoder.matches(emailMapper.getPassword(), user.getPassword())
                        ?Mono.error(new SamePasswordsException())
                        :Mono.just(user))
                .flatMap(user -> {
                    user.setPassword(this.passwordEncoder.encode(emailMapper.getPassword()));
                    user.setUpdateAt(new Date());
                    return this.userDAO.save(user);
                })
                .switchIfEmpty(Mono.error(new BadCredentialsException()));
    }

    @Override
    public Mono<String> validateAccount(String token) {
        return TokenUtils.authenticationToken(token)
                .flatMap(claims -> this.userDAO.findOneByEmail(claims.getSubject())
                        .flatMap(user -> user.isVerified()?Mono.error(new ErrorValidatingAccountException()):Mono.just(user))
                        .flatMap(user -> {
                            user.setVerified(true);
                            return this.userDAO.save(user)
                                    .flatMap(u -> Mono.just("Verified account"));
                        })
                .switchIfEmpty(Mono.error(new BadCredentialsException())));

    }

    @Override
    public Mono<Map<String, String>> reSendToken(String email) {
        return this.userDAO.findOneByEmail(email)
                .flatMap(user -> user.isVerified()?Mono.error(new ErrorValidatingAccountException())
                        :Mono.just(user))
                .flatMap(user -> TokenUtils.generateToken(new UserAuth(
                        user.getEmail(),null,user.getRoles()), 5))
                        .map(token -> Map.of("token verification", token))
                .switchIfEmpty(Mono.error(new BadCredentialsException()));
    }
}
