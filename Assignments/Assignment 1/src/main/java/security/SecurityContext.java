package security;

import model.User;

public class SecurityContext {
    private User currentUser;
    private static SecurityContext singleInstance = new SecurityContext();

    private SecurityContext() {
    }

    public static User getCurrentUser() {
        return singleInstance.currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        singleInstance.currentUser = currentUser;
    }

    public static void unsetCurrentUser()
    {
        singleInstance.currentUser = null;
    }

    public static SecurityContext getSingleInstance() {
        return singleInstance;
    }

}
