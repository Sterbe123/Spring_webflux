package cl.sterbe.app.services.users.implement;

import cl.sterbe.app.documents.dao.users.UserDAO;
import cl.sterbe.app.documents.models.users.User;
import cl.sterbe.app.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserImplement implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public Flux<User> findAll() {
        return this.userDAO.findAll();
    }

    @Override
    public Mono<User> findById(String id) {
        return this.userDAO.findById(id);
    }

    @Override
    public Mono<User> save(User user) {
        return this.userDAO.save(user);
    }

    @Override
    public Mono<Void> delete(User user) {
        return this.userDAO.delete(user);
    }
}
