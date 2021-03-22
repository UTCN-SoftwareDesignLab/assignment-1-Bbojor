package launcher;

import database.Boostrap;

import java.sql.SQLException;

/**
 * Created by Alex on 18/03/2017.
 */
public class Launcher {

    public static boolean BOOTSTRAP = false;
    public static boolean TESTING = false;

    public static void main(String[] args) {
        bootstrap();

        ComponentFactory componentFactory = ComponentFactory.instance(TESTING);
        componentFactory.getControllerSwitcher().init();
    }

    private static void bootstrap() {
        if (BOOTSTRAP) {
            try {
                new Boostrap().execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

}
