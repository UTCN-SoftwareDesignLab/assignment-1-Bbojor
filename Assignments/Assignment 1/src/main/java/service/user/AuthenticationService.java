package service.user;

import model.Role;
import model.User;
import model.validation.Notification;

import java.util.List;

/**
 * Created by Alex on 11/03/2017.
 */
public interface AuthenticationService {

    Notification<Boolean> register(String username, String password, List<String> roles);

    Notification<User> login(String username, String password);

    void logout();

}
