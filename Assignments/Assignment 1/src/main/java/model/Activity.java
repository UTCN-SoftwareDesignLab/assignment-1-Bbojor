package model;

import java.util.Date;

public class Activity {

    private Long id;
    private String user;
    private String type;
    private String arguments;
    private Date date;

    public static class ActivityTypes {
        public static String ADD_CLIENT = "add client";
        public static String UPDATE_CLIENT = "update client";
        public static String DELETE_CLIENT = "delete client";
        public static String ADD_ACCOUNT = "add account";
        public static String UPDATE_ACCOUNT = "update account";
        public static String DELETE_ACCOUNT = "delete account";
        public static String TRANSFER = "transfer money";
        public static String PROCESS_BILL = "process bill";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getArguments() {
        return arguments;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}



