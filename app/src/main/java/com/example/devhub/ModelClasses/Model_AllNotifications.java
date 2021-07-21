package com.example.devhub.ModelClasses;

public class Model_AllNotifications {
    String action, email,type;

    public Model_AllNotifications() {
    }

    public Model_AllNotifications(String action, String email, String type) {
        this.action = action;
        this.email = email;
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
