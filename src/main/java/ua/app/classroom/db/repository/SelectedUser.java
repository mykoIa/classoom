package ua.app.classroom.db.repository;

import ua.app.classroom.model.entity.User;

public class SelectedUser {
    private static User user;
    private static String fullName;
    private static boolean roleAdmin;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) { SelectedUser.user = user;
    }

    public static String getFullName() {
        return fullName;
    }

    public static void setFullName(String fullName) {
        SelectedUser.fullName = fullName;
    }

    public static boolean isRoleAdmin() {
        return roleAdmin;
    }

    public static void setRoleAdmin(boolean roleAdmin) {
        SelectedUser.roleAdmin = roleAdmin;
    }

    public static void clear() {
        setUser(new User());
        setFullName("");
        setRoleAdmin(false);
    }
}

