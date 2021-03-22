package launcher;

import controller.AdminController;
import controller.EmployeeController;
import controller.LoginController;
import database.DBConnectionFactory;
import repository.account.AccountRepository;
import repository.account.AccountRepositoryMySQL;
import repository.activity.ActivityRepository;
import repository.activity.ActivityRepositoryMySQL;
import repository.bill.BillRepository;
import repository.bill.BillRepositoryMySQL;
import repository.client.ClientRepository;
import repository.client.ClientRepositoryMySQL;
import repository.permission.RightsRolesRepository;
import repository.permission.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.account.AccountService;
import service.account.AccountServiceMySQL;
import service.activity.ActivityService;
import service.activity.ActivityServiceMySQL;
import service.bill.BillService;
import service.bill.BillServiceMySQL;
import service.client.ClientService;
import service.client.ClientServiceMySQL;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceMySQL;
import service.user.UserService;
import service.user.UserServiceMySQL;
import view.AdminView;
import view.EmployeeView;
import view.LoginView;

import java.sql.Connection;

/**
 * Created by Alex on 18/03/2017.
 */
public class ComponentFactory {

    private static ComponentFactory instance;

    private final ControllerSwitcher controllerSwitcher;

    public static ComponentFactory instance(Boolean componentsForTests) {
        if (instance == null) {
            instance = new ComponentFactory(componentsForTests);
        }
        return instance;
    }

    private ComponentFactory(Boolean componentsForTests) {
        Connection connection = new DBConnectionFactory().getConnectionWrapper(componentsForTests).getConnection();

        RightsRolesRepository rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        UserRepository userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
        ActivityRepository activityRepository = new ActivityRepositoryMySQL(connection);

        AuthenticationService authenticationService = new AuthenticationServiceMySQL(userRepository, rightsRolesRepository);
        UserService userService = new UserServiceMySQL(userRepository);
        ActivityService activityService = new ActivityServiceMySQL(activityRepository);

        AdminView adminView = new AdminView();
        LoginView loginView = new LoginView();

        AdminController adminController = new AdminController(adminView, authenticationService, userService, activityService);
        LoginController loginController = new LoginController(loginView, authenticationService);

        EmployeeView employeeView = new EmployeeView();
        ClientRepository clientRepository = new ClientRepositoryMySQL(connection);
        ClientService clientService = new ClientServiceMySQL(clientRepository);
        AccountRepository accountRepository = new AccountRepositoryMySQL(connection);
        AccountService accountService = new AccountServiceMySQL(accountRepository);
        BillRepository billRepository = new BillRepositoryMySQL(connection);
        BillService billService = new BillServiceMySQL(billRepository, accountRepository);

        EmployeeController employeeController = new EmployeeController(employeeView, clientService, accountService, billService, authenticationService, activityService);

        this.controllerSwitcher = new ControllerSwitcher(loginController, adminController, employeeController);

    }

    public ControllerSwitcher getControllerSwitcher() {
        return controllerSwitcher;
    }

    public static ComponentFactory getInstance() {
        return instance;
    }
}
