package service.user;

import model.User;
import model.validation.Notification;
import model.validation.UserValidator;
import repository.user.UserRepository;
import security.PasswordEncoder;

import java.util.List;

public class UserServiceMySQL implements UserService {

    private final UserRepository userRepository;

    public UserServiceMySQL(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Notification<Boolean> updateUser(User user) {

        Notification<User> oldUserNotification = userRepository.findById(user.getId());
        Notification<Boolean> result = new Notification<>();

        if(oldUserNotification.hasErrors())
        {
            result.addError(oldUserNotification.getFormattedErrors());
            result.setResult(Boolean.FALSE);
            return result;
        }

        User oldUser = oldUserNotification.getResult();

        boolean shouldCheckPassword = true, shouldCheckUsername = true;

        if(user.getUsername() == null || user.getUsername().isBlank()) {
            user.setUsername(oldUser.getUsername());
            shouldCheckUsername = false;
        }

        if(user.getPassword() == null || user.getPassword().isBlank()){
            user.setPassword(oldUser.getPassword());
            shouldCheckPassword = false;
        }

        UserValidator userValidator = new UserValidator(user);

        if(shouldCheckUsername)
            userValidator.validateUsername(user.getUsername());

        if(shouldCheckPassword)
            userValidator.validatePassword(user.getPassword());

        boolean userValid = userValidator.getErrors().isEmpty();

        if (!userValid) {
            userValidator.getErrors().forEach(result::addError);
            result.setResult(Boolean.FALSE);
        } else {
            if(shouldCheckPassword) //only encode password if it was changed
                user.setPassword(PasswordEncoder.encodePassword(user.getPassword()));
            result.setResult(userRepository.updateUser(user));
        }
        return result;
    }

    @Override
    public Notification<Boolean> deleteUser(User user) {
        Notification<Boolean> result = new Notification<>();
        result.setResult(userRepository.deleteUser(user));
        return result;
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }

}
