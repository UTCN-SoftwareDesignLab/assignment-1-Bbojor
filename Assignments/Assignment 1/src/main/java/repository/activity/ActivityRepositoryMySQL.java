package repository.activity;

import model.Activity;
import model.User;
import model.builder.ActivityBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Tables.ACCOUNT;
import static database.Constants.Tables.ACTIVITY;

public class ActivityRepositoryMySQL implements ActivityRepository {

    private final Connection connection;

    public ActivityRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Activity> getAll() {
        List<Activity> result = new ArrayList<>();
        try {
            Statement getAllActivityStatement = connection.createStatement();
            ResultSet activityResultSet = getAllActivityStatement.executeQuery("Select * from " + ACTIVITY);
            while(activityResultSet.next()) {
                Activity activity = new ActivityBuilder()
                        .setId(activityResultSet.getLong("id"))
                        .setUser(activityResultSet.getString("user_username"))
                        .setType(activityResultSet.getString("type"))
                        .setArguments(activityResultSet.getString("arguments"))
                        .setDate(new Date(activityResultSet.getDate("date").getTime()))
                        .build();
                result.add(activity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<Activity> getUserActivity(User user) {
        List<Activity> result = new ArrayList<>();
        try {
            Statement getUserActivityStatement = connection.createStatement();
            ResultSet activityResultSet = getUserActivityStatement.executeQuery("Select * from " + ACTIVITY + " where user_username=\"" + user.getUsername() + "\";");
            while(activityResultSet.next()) {
                Activity activity = new ActivityBuilder()
                        .setId(activityResultSet.getLong("id"))
                        .setUser(activityResultSet.getString("user_username"))
                        .setType(activityResultSet.getString("type"))
                        .setArguments(activityResultSet.getString("arguments"))
                        .setDate(new Date(activityResultSet.getDate("date").getTime()))
                        .build();
                result.add(activity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public boolean addActivity(Activity activity) {
        try {
            PreparedStatement insertActivity = connection.prepareStatement("Insert into  " + ACTIVITY + " values (null, ? , ?, ?, ?);");
            insertActivity.setString(1, activity.getUser());
            insertActivity.setString(2, activity.getType());
            insertActivity.setString(3, activity.getArguments());
            insertActivity.setDate(4, new java.sql.Date(activity.getDate().getTime()));
            insertActivity.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public void deleteAll() {
        try {
            Statement statement = connection.createStatement();
            String fetchUserSql = "DELETE from `" + ACTIVITY  + "`;";
            statement.executeUpdate(fetchUserSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
