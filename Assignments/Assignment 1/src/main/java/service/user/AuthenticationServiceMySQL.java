package service.user;

import model.Role;
import model.User;
import model.builder.UserBuilder;
import model.validation.Notification;
import model.validation.UserValidator;
import repository.permission.RightsRolesRepository;
import repository.user.UserRepository;
import security.PasswordEncoder;
import security.SecurityContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 11/03/2017.
 */
public class AuthenticationServiceMySQL implements AuthenticationService {

    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;

    public AuthenticationServiceMySQL(UserRepository userRepository, RightsRolesRepository rightsRolesRepository) {
        this.userRepository = userRepository;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public Notification<Boolean> register(String username, String password, List<String> roles) {

        List<Role> actualRoles = new ArrayList<>();
        for(String r : roles){
            actualRoles.add(rightsRolesRepository.findRoleByTitle(r));
        }

        User user = new UserBuilder()
                .setUsername(username)
                .setPassword(password)
                .setRoles(actualRoles)
                .build();

        UserValidator userValidator = new UserValidator(user);
        boolean userValid = userValidator.validate();
        Notification<Boolean> userRegisterNotification = new Notification<>();

        if (!userValid) {
            userValidator.getErrors().forEach(userRegisterNotification::addError);
            userRegisterNotification.setResult(Boolean.FALSE);
        } else {
            user.setPassword(PasswordEncoder.encodePassword(password));
            userRegisterNotification.setResult(userRepository.save(user));
        }
        return userRegisterNotification;
    }

    @Override
    public Notification<User> login(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, PasswordEncoder.encodePassword(password));
    }

    @Override
    public void logout() {
        SecurityContext.unsetCurrentUser();
    }

}
