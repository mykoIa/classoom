package ua.app.classroom.util;

import ua.app.classroom.model.entity.User;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.Locale;

public class SendMessage {

    public static boolean loginOrPasswordIsEmpty(User user, String password) {
        if (user.getFullName().isEmpty()) {
            setErrorMessage("Login can't be empty");
            return true;
        }
        if (password.isEmpty()) {
            setErrorMessage("Password can't be empty");
            return true;
        }
        return false;
    }

    public static boolean chekLogin(String fullName) {
        if (fullName.trim().isEmpty()) {
            setErrorMessage("Login can't be empty");
            return true;
        }
        return false;
    }

    public static boolean checkRole(User user) {
        user.setRole(user.getRole().toUpperCase(Locale.ROOT));
        if (!user.getRole().equals("USER") && !user.getRole().equals("ADMIN")) {
            setErrorMessage("Wrong role format must be USER or ADMIN ");
            return true;
        }
        return false;
    }

    public static void loginIsAlreadyTaken() {
        setErrorMessage("This login is already taken");
    }

    public static void sendMessageWhenAuthorizeFailed() {
        setErrorMessage("Login or password is incorrect");
    }

    public static void userAdded() {
        setInfoMessage("User added");
    }

    private static void setErrorMessage(String errorMessage) {
        FacesContext.getCurrentInstance().addMessage("loginMsg",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", errorMessage));
    }

    private static void setInfoMessage(String infoMessage) {
        FacesContext.getCurrentInstance().addMessage("loginMsg",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", infoMessage));
    }
}
