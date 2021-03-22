package service.user;

import model.User;
import model.validation.Notification;

import java.util.List;

public interface UserService {

    public List<User> findAll();

    public Notification<Boolean> updateUser(User user);

    public Notification<Boolean> deleteUser(User user);

    public void deleteAll();

}
