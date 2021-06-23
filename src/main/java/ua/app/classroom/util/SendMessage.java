package ua.app.classroom.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class SendMessage {

    public static void loginIsEmpty() {
        setErrorMessage("Login can't be empty");
    }

    public static void passwordIsEmpty() {
        setErrorMessage("Password can't be empty");
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
