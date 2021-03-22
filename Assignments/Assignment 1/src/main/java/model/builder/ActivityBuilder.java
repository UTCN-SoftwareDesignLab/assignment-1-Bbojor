package model.builder;

import model.Activity;

import java.util.Date;

/**
 * Created by Alex on 11/03/2017.
 */
public class ActivityBuilder {

    private Activity activity;

    public ActivityBuilder() {
        activity =  new Activity();
    }

    public ActivityBuilder setType(String type) {
        activity.setType(type);
        return this;
    }

    public ActivityBuilder setDate(Date date) {
        activity.setDate(date);
        return this;
    }

    public ActivityBuilder setUser(String user) {
        activity.setUser(user);
        return this;
    }

    public ActivityBuilder setId(Long id) {
        activity.setId(id);
        return this;
    }

    public ActivityBuilder setArguments(String arguments)
    {
        activity.setArguments(arguments);
        return this;
    }

    public Activity build() {
        return activity;
    }


}
