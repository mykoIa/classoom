package ua.app.classroom.service;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.ContextLoader;
import ua.app.classroom.db.UserDB;
import ua.app.classroom.db.UserMap;
import ua.app.classroom.entity.User;
import ua.app.classroom.util.ErrorMessage;
import ua.app.classroom.websocket.WebSocket;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Named
@SessionScoped
public class UserService implements Serializable {

    public static final String LOGIN_FACES_REDIRECT_TRUE = "login?faces-redirect=true";
    private static final Logger LOG = Logger.getLogger(UserService.class);

    @Inject
    private UserMap userMap;

    @Inject
    private WebSocket webSocket;

    private User user = new User();
    private String password;

    public String singIn() {
        UserDB userDB = (UserDB) Objects.requireNonNull(ContextLoader.getCurrentWebApplicationContext()).getBean("userDB");
        user.setFullName(user.getFullName().trim());
        if (ErrorMessage.loginOrPasswordIsEmpty(user, password)) {
            return "";
        }
        if (userDB.userIsExist(user)) {
            LOG.trace("Login is already taken");
            ErrorMessage.setMessage("This login is already taken");
            return "";
        }
        userDB.addUserToDB(user, password);
        LOG.trace("Method registrationUser completed successfully");
        return LOGIN_FACES_REDIRECT_TRUE;
    }

    public String logout() {
        userMap.removeUser(user);
        webSocket.userDisconnected(user.getFullName());
        LOG.trace("Method logOut completed successfully");
        return "/login?faces-redirect=true";
    }

    public String login() {
        user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userMap.addUserToMap(user);
        webSocket.userConnected(user.getFullName());
        LOG.trace("Method authorizeUser completed successfully");
        return "/secure/members?faces-redirect=true";
    }

    public String errorLogin () {
        ErrorMessage.sendMessageWhenAuthorizeFailed();
        return "/login?faces-redirect=true";
    }

    public void handUpDown() {
        user.setHandUp(!user.isHandUp());
        if (user.isHandUp()) {
            webSocket.userHandUp(user.getFullName());
        } else {
            webSocket.userHandDown(user.getFullName());
        }
    }

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
        return userMap.getUserList();
    }

    public void setUserMap(UserMap userMap) {
        this.userMap = userMap;
    }
}
