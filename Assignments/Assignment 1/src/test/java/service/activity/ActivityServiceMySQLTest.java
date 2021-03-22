package service.activity;

import database.Constants;
import database.DBConnectionFactory;
import model.Activity;
import model.User;
import model.builder.ActivityBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.activity.ActivityRepository;
import repository.activity.ActivityRepositoryMySQL;
import repository.permission.RightsRolesRepository;
import repository.permission.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceMySQL;
import service.user.UserService;
import service.user.UserServiceMySQL;

import java.sql.Connection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

class ActivityServiceMySQLTest {

    private static ActivityService activityService;
    private static UserService userService;
    private static AuthenticationService authenticationService;

    private static User user1, user2;

    @BeforeAll
    static void setup() {
        Connection connection = new DBConnectionFactory().getConnectionWrapper(true).getConnection();
        ActivityRepository activityRepository = new ActivityRepositoryMySQL(connection);
        activityService = new ActivityServiceMySQL(activityRepository);

        //get a user to add activities to
        RightsRolesRepository rightsRoleRepository = new RightsRolesRepositoryMySQL(connection);
        UserRepository userRepository = new UserRepositoryMySQL(connection, rightsRoleRepository);
        userService = new UserServiceMySQL(userRepository);
        authenticationService = new AuthenticationServiceMySQL(userRepository, rightsRoleRepository);

        userService.deleteAll();
        authenticationService.register("user@mail.com", "Pass_123", Collections.singletonList(Constants.Roles.EMPLOYEE));
        user1 = userRepository.findAll().get(0);

        authenticationService.register("use21r@mail.com", "Pass_123", Collections.singletonList(Constants.Roles.EMPLOYEE));
        user2 = userRepository.findAll().get(1);
    }

    @BeforeEach
    void clean() {
        activityService.deleteAll();
    }

    @Test
    void getAll() {
        int NO_ACTIVITIES = 10;
        for(int i = 0; i < NO_ACTIVITIES; i++) {

            Activity activity = new ActivityBuilder()
                    .setArguments("arguments here")
                    .setDate(new Date())
                    .setUser(user1.getUsername())
                    .setType(Activity.ActivityTypes.TRANSFER)
                    .build();

            activityService.addActivity(activity);
        }

        List<Activity> activities = activityService.getAll();

        Assertions.assertEquals(NO_ACTIVITIES, activities.size());
    }

    @Test
    void getUserActivity() {
        int NO_ACTIVITIES = 10;
        for(int i = 0; i < NO_ACTIVITIES; i++) {

            Activity activity = new ActivityBuilder()
                    .setArguments("arguments here")
                    .setDate(new Date())
                    .setUser(user1.getUsername())
                    .setType(Activity.ActivityTypes.TRANSFER)
                    .build();

            activityService.addActivity(activity);
        }

        for(int i = 0; i < NO_ACTIVITIES; i++) {

            Activity activity = new ActivityBuilder()
                    .setArguments("arguments here")
                    .setDate(new Date())
                    .setUser(user2.getUsername())
                    .setType(Activity.ActivityTypes.TRANSFER)
                    .build();

            activityService.addActivity(activity);
        }

        List<Activity> activities = activityService.getUserActivity(user1);

        Assertions.assertEquals(NO_ACTIVITIES, activities.size());
    }


    @Test
    void deleteAll() {
        int NO_ACTIVITIES = 10;
        for(int i = 0; i < NO_ACTIVITIES; i++) {

            Activity activity = new ActivityBuilder()
                    .setArguments("arguments here")
                    .setDate(new Date())
                    .setUser(user1.getUsername())
                    .setType(Activity.ActivityTypes.TRANSFER)
                    .build();

            activityService.addActivity(activity);
        }

        List<Activity> activities = activityService.getAll();

        Assertions.assertEquals(NO_ACTIVITIES, activities.size());
    }
}