package ua.app.classroom.db.repository;

import ua.app.classroom.model.entity.User;

public class CurrentUser {

    private static User user;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        CurrentUser.user = user;
    }
}
