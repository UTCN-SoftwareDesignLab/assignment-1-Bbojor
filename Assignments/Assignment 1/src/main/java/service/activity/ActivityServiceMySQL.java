package service.activity;

import model.Activity;
import model.User;
import model.validation.Notification;
import repository.activity.ActivityRepository;

import java.util.List;

public class ActivityServiceMySQL implements ActivityService {

    private final ActivityRepository activityRepository;

    public ActivityServiceMySQL(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public List<Activity> getAll() {
        return activityRepository.getAll();
    }

    @Override
    public List<Activity> getUserActivity(User user) {
        return activityRepository.getUserActivity(user);
    }


    @Override
    public Notification<Boolean> addActivity(Activity activity) {
        Notification<Boolean> result = new Notification<>();
        result.setResult(activityRepository.addActivity(activity));
        return result;
    }


    @Override
    public void deleteAll() {
        activityRepository.deleteAll();
    }
}