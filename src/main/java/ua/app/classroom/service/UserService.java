package ua.app.classroom.service;

import ua.app.classroom.db.UserDB;
import ua.app.classroom.model.User;
import ua.app.classroom.websocket.WebSocket;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collection;

@Named
@SessionScoped
public class UserService implements Serializable {

    public static final String MEMBERS_FACES_REDIRECT_TRUE = "members?faces-redirect=true";
    public static final String LOGIN_FACES_REDIRECT_TRUE = "login?faces-redirect=true";

    @Inject
    private UserDB userDB;

    @Inject
    private WebSocket webSocket;

    private User user = new User();
    private String password;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void clearUser() {
        this.user = new User();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<User> getUserList() {
        return userDB.getUserList();
    }

    public String registrationUser() {
        if (loginAndPasswordIsEmpty(user)) {
            return "";
        }
        if (userDB.findUser(user, password)) {
            setErrorMessage("This login is already taken");
            return "";
        }
        user.setUserAlreadySignedUp(true);
        userDB.addUser(user, password);
        password = "";
        webSocket.userConnected(user.getFullName());
        return MEMBERS_FACES_REDIRECT_TRUE;
    }

    public String logOut() {
        user.setUserAlreadySignedUp(false);
        userDB.updateUser(user, false);
        webSocket.userDisconnected(user.getFullName());
        return LOGIN_FACES_REDIRECT_TRUE;
    }

    public String authorizeUser() {
        if (loginAndPasswordIsEmpty(user)) {
            return "";
        }
        if (!userDB.findUser(user, password)) {
            setErrorMessage("Login or password is incorrect");
            return "";
        }
        password = "";
        user.setUserAlreadySignedUp(true);
        userDB.updateUser(user, true);
        webSocket.userConnected(user.getFullName());
        return MEMBERS_FACES_REDIRECT_TRUE;
    }

    public String redirectToLogin() {
        return user.isUserAlreadySignedUp() ? "" : LOGIN_FACES_REDIRECT_TRUE;
    }

    public String redirectToMembers() {
        return user.isUserAlreadySignedUp() ? MEMBERS_FACES_REDIRECT_TRUE : "";
    }

    public void handUpDown() {
        user.setHandUp(!user.isHandUp());
        if (user.isHandUp()) {
            webSocket.userHandUp(user.getFullName());
        } else {
            webSocket.userHandDown(user.getFullName());
        }
    }

    private void setErrorMessage(String errorMessage) {
        FacesContext.getCurrentInstance().addMessage("loginMsg",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", errorMessage));
    }

    private boolean loginAndPasswordIsEmpty(User user) {
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

}
