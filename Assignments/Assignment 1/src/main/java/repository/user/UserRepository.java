package repository.user;

import model.User;
import model.validation.Notification;

import java.util.List;

/**
 * Created by Alex on 11/03/2017.
 */
public interface UserRepository {

    List<User> findAll();

    Notification<User> findByUsernameAndPassword(String username, String password);

    Notification<User> findById(Long id);

    boolean save(User user);

    boolean updateUser(User user);

    boolean deleteUser(User user);

    void removeAll();

    void deleteAll();
}
