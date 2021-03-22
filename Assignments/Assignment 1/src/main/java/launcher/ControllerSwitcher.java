package launcher;

import controller.AdminController;
import controller.EmployeeController;
import controller.LoginController;
import database.Constants;
import model.Role;
import model.User;
import security.SecurityContext;

import java.util.List;

public class ControllerSwitcher {

    private final LoginController loginController;
    private final AdminController adminController;
    private final EmployeeController employeeController;

    public ControllerSwitcher(LoginController loginController, AdminController adminController, EmployeeController employeeController) {
        this.loginController = loginController;
        this.adminController = adminController;
        this.employeeController = employeeController;
    }

    public void init()
    {
        loginController.makeActive();
    }

    public void switchController()
    {
        User currentUser = SecurityContext.getCurrentUser();

        if(currentUser == null) {
            adminController.hide();
            employeeController.hide();
            loginController.makeActive();
        }
        else
        {
            loginController.hide();

            List<Role> roles = currentUser.getRoles();

            for(Role r:roles)
            {
                switch (r.getRole()) {
                    case Constants.Roles.EMPLOYEE -> employeeController.makeActive();
                    case Constants.Roles.ADMINISTRATOR -> adminController.makeActive();
                }
            }
        }

    }

}
