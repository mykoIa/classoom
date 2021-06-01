package ua.app.classroom.util;

import ua.app.classroom.entity.User;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class ErrorMessage {

    public static void setMessage(String errorMessage) {
        FacesContext.getCurrentInstance().addMessage("loginMsg",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", errorMessage));
    }

    public static boolean loginOrPasswordIsEmpty(User user, String password) {
        if (user.getFullName().isEmpty()) {
            setMessage("Login can't be empty");
            return true;
        }
        if (password.isEmpty()) {
            setMessage("Password can't be empty");
            return true;
        }
        return false;
    }

    public static void sendMessageWhenAuthorizeFailed() {
        setMessage("Login or password is incorrect");
    }
}
