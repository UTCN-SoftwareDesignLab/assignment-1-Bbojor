package service.user;

import database.Constants;
import database.DBConnectionFactory;
import model.User;
import model.builder.UserBuilder;
import model.validation.Notification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.permission.RightsRolesRepository;
import repository.permission.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceMySQLTest {

    private static UserService userService;
    private static AuthenticationService authenticationService;

    @BeforeAll
    private static void setup() {
        Connection connection = new DBConnectionFactory().getConnectionWrapper(true).getConnection();
        RightsRolesRepository rightsRoleRepository = new RightsRolesRepositoryMySQL(connection);
        UserRepository userRepository = new UserRepositoryMySQL(connection, rightsRoleRepository);
        userService = new UserServiceMySQL(userRepository);
        authenticationService = new AuthenticationServiceMySQL(userRepository, rightsRoleRepository);
    }

    @BeforeEach
    private void clear() {
        userService.deleteAll();
    }

    @Test
    void findAll() {

        int NO_USERS = 10;

        for(int i = 0; i < NO_USERS; i++) {

            authenticationService.register("user" + i + "@mail.com", "Pass_123", Collections.singletonList(Constants.Roles.EMPLOYEE));
        }

        List<User> users = userService.findAll();

        Assertions.assertEquals(NO_USERS, users.size());

        for(int i = 0; i < NO_USERS; i++) {
            User u = users.get(i);
            Assertions.assertEquals("user" + i + "@mail.com", u.getUsername());
        }
    }

    @Test
    void updateUser() {
        authenticationService.register("user@mail.com", "Pass_123", Collections.singletonList(Constants.Roles.EMPLOYEE));

        Notification<User> loginNotification = authenticationService.login("user@mail.com", "Pass_123");

        assertFalse(loginNotification.hasErrors());

        User user = loginNotification.getResult();
        user.setUsername("newuser@mail.com");
        user.setPassword("NewPass_123");

        Notification<Boolean> updateNotification = userService.updateUser(user);

        assertFalse(updateNotification.hasErrors());
        assertTrue(updateNotification.getResult());

        loginNotification = authenticationService.login("newuser@mail.com", "NewPass_123");

        assertFalse(loginNotification.hasErrors());

    }

    @Test
    void deleteUser() {

        authenticationService.register("user@mail.com", "Pass_123", Collections.singletonList(Constants.Roles.EMPLOYEE));

        Notification<User> loginNotification = authenticationService.login("user@mail.com", "Pass_123");

        assertFalse(loginNotification.hasErrors());

        User user = loginNotification.getResult();

        userService.deleteUser(user);

        List<User> users = userService.findAll();

        assertTrue(users.isEmpty());
    }

    @Test
    void deleteAll() {
        int NO_USERS = 10;

        for(int i = 0; i < NO_USERS; i++) {

            authenticationService.register("user" + i + "@mail.com", "Pass_123", Collections.singletonList(Constants.Roles.EMPLOYEE));
        }

        List<User> users = userService.findAll();
        Assertions.assertEquals(NO_USERS, users.size());

        userService.deleteAll();

        users = userService.findAll();
        assertTrue(users.isEmpty());
    }
}