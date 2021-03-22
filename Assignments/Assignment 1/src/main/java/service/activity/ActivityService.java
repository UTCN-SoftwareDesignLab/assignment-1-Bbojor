package service.activity;

import model.Activity;
import model.User;
import model.validation.Notification;

import java.util.List;

public interface ActivityService {

    public List<Activity> getAll();

    public List<Activity> getUserActivity(User user);

    public Notification<Boolean> addActivity(Activity activity);

    public void deleteAll();
}
