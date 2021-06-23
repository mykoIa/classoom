package ua.app.classroom.util;

import ua.app.classroom.model.entity.User;

public class VerifyLoginAndPassword {

    public static boolean loginOrPasswordIsEmpty(User user, String password) {
        if (user.getFullName().isEmpty()) {
            SendMessage.loginIsEmpty();
            return true;
        }
        if (password.isEmpty()) {
            SendMessage.passwordIsEmpty();
            return true;
        }
        return false;
    }

    public static boolean loginIsEmpty(String fullName) {
        if (fullName.trim().isEmpty()) {
            SendMessage.loginIsEmpty();
            return true;
        }
        return false;
    }
}
