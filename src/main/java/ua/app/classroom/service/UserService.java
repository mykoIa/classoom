package ua.app.classroom.service;

import org.apache.log4j.Logger;
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
    private static final Logger LOG = Logger.getLogger(UserDB.class);

    @Inject
    private UserDB userDB;

    @Inject
    private WebSocket webSocket;

    private User user = new User();
    private String password;
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
        user.setFullName(user.getFullName().trim());
        if (loginOrPasswordIsEmpty(user)) {
            return "";
        }
        if (userDB.findUserByName(user)) {
            LOG.trace("Login is already taken");
            setErrorMessage("This login is already taken");
            return "";
        }
        userAlreadySignedUp = true;
        userDB.addUserToDB(user, password);
        userDB.addUserToMap(user);
        password = "";
        webSocket.userConnected(user.getFullName());
        LOG.trace("Method registrationUser completed successfully");
        return MEMBERS_FACES_REDIRECT_TRUE;
    }

    public String logOut() {
        userDB.removeUser(user);
        userAlreadySignedUp = false;
        webSocket.userDisconnected(user.getFullName());
        LOG.trace("Method logOut completed successfully");
        return LOGIN_FACES_REDIRECT_TRUE;
    }

    public String authorizeUser() {
        if (loginOrPasswordIsEmpty(user)) {
            return "";
        }
        if (!userDB.findUser(user, password)) {
            LOG.trace("Login or password is incorrect");
            setErrorMessage("Login or password is incorrect");
            return "";
        }
        password = "";
        userDB.addUserToMap(user);
        userAlreadySignedUp = true;
        webSocket.userConnected(user.getFullName());
        LOG.trace("Method authorizeUser completed successfully");
        return MEMBERS_FACES_REDIRECT_TRUE;
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

    private boolean loginOrPasswordIsEmpty(User user) {
        if (user.getFullName().isEmpty()) {
            LOG.trace("Name is empty");
            setErrorMessage("Login can't be empty");
            return true;
        }
        if (password.isEmpty()) {
            LOG.trace("Password is empty");
            setErrorMessage("Password can't be empty");
            return true;
        }
        return false;
    }

}
