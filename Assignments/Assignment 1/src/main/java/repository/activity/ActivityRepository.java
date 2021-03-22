package repository.activity;

import model.Activity;
import model.User;

import java.util.List;

public interface ActivityRepository {

    public List<Activity> getAll();

    public List<Activity> getUserActivity(User user);

    public boolean addActivity(Activity activity);



    void deleteAll();
}
