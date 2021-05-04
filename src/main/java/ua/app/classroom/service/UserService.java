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
    private boolean userAlreadySignedUp;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void clearUser() {
        this.user = new User();
    }

    public Collection<User> getUserList() {
        return userDB.getUserList();
    }

    public String authorizeUser() {
        if (userAlreadySignedUp) {
            return MEMBERS_FACES_REDIRECT_TRUE;
        }
        user.setFullName(user.getFullName().trim());
        if (user.getFullName().isEmpty()) {
            setErrorMessage("Login can't be empty");
            return "";
        }
        if (userDB.isFullNameInUse(user.getFullName())) {
            setErrorMessage("This login is already taken");
            return "";
        }
        userDB.addUser(user);
        userAlreadySignedUp = true;
        webSocket.userConnected(user.getFullName());
        return MEMBERS_FACES_REDIRECT_TRUE;
    }

    public String logOut() {
        userDB.removeUser(user);
        userAlreadySignedUp = false;
        webSocket.userDisconnected(user.getFullName());
        return LOGIN_FACES_REDIRECT_TRUE;
    }

    public String redirectToLogin() {
        return userAlreadySignedUp ? "" : LOGIN_FACES_REDIRECT_TRUE;
    }

    public String redirectToMembers() {
        return userAlreadySignedUp ? MEMBERS_FACES_REDIRECT_TRUE : "";
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

}
