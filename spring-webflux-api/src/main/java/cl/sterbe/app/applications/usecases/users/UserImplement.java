package cl.sterbe.app.applications.usecases.users;

import cl.sterbe.app.domains.exceptions.*;
import cl.sterbe.app.domains.models.auths.UserAuth;
import cl.sterbe.app.domains.models.email.EmailMapper;
import cl.sterbe.app.domains.models.users.User;
import cl.sterbe.app.domains.ports.in.users.UserUseCase;
import cl.sterbe.app.domains.ports.out.users.RoleRepositoryPort;
import cl.sterbe.app.domains.ports.out.users.UserRepositoryPort;
import cl.sterbe.app.infrastructure.components.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

@Service
public class UserImplement implements UserUseCase {

    @Autowired
    private UserRepositoryPort userRepositoryPort;

    @Autowired
    private RoleRepositoryPort roleRepositoryPort;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Flux<User> findAll() {
        return this.userRepositoryPort.findAll();
    }

    @Override
    public Mono<User> findById(String id) {
        return this.userRepositoryPort.findById(id)
                .switchIfEmpty(Mono.error(new UserNotFoundException()));
    }

    @Override
    public Mono<User> save(User user) {
        return this.userRepositoryPort.save(user);
    }

    @Override
    public Mono<Void> delete(String id) {
        return this.userRepositoryPort.findById(id)
                .flatMap(user -> this.userRepositoryPort.delete(user))
                .switchIfEmpty(Mono.error(new UserNotFoundException()));
    }

    @Override
    public Mono<User> findOneByEmail(String email) {
        return this.userRepositoryPort.findOneByEmail(email)
                .switchIfEmpty(Mono.error(new UserNotFoundException()));
    }

    @Override
    public Mono<User> login(EmailMapper emailMapper, ServerWebExchange serverWebExchange) {
        return this.userRepositoryPort.findOneByEmail(emailMapper.getEmail())
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
        return this.userRepositoryPort.findOneByEmail(emailMapper.getEmail()).hasElement()
                .flatMap(exists -> exists?Mono.error(new EmailAlreadyExistsException())
                        :this.roleRepositoryPort.findOneByName("ROLE_USER")
                        .flatMap(role -> this.userRepositoryPort.save(new User(
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
                        :this.userRepositoryPort.findOneByEmail(emailMapper.getEmail()))
                .flatMap(user -> this.passwordEncoder.matches(emailMapper.getPassword(), user.getPassword())
                        ?Mono.error(new SamePasswordsException())
                        :Mono.just(user))
                .flatMap(user -> {
                    user.setPassword(this.passwordEncoder.encode(emailMapper.getPassword()));
                    user.setUpdateAt(new Date());
                    return this.userRepositoryPort.save(user);
                })
                .switchIfEmpty(Mono.error(new BadCredentialsException()));
    }

    @Override
    public Mono<String> validateAccount(String token) {
        return TokenUtils.authenticationToken(token)
                .flatMap(claims -> this.userRepositoryPort.findOneByEmail(claims.getSubject())
                        .flatMap(user -> user.isVerified()?Mono.error(new ErrorValidatingAccountException()):Mono.just(user))
                        .flatMap(user -> {
                            user.setVerified(true);
                            return this.userRepositoryPort.save(user)
                                    .flatMap(u -> Mono.just("Verified account"));
                        })
                .switchIfEmpty(Mono.error(new BadCredentialsException())));

    }

    @Override
    public Mono<Map<String, String>> reSendToken(String email) {
        return this.userRepositoryPort.findOneByEmail(email)
                .flatMap(user -> user.isVerified()?Mono.error(new ErrorValidatingAccountException())
                        :Mono.just(user)).cast(User.class)
                .flatMap(user -> TokenUtils.generateToken(new UserAuth(
                        user.getEmail(),null,user.getRoles()), 5))
                        .map(token -> Map.of("token verification", token))
                .switchIfEmpty(Mono.error(new BadCredentialsException()));
    }
}
