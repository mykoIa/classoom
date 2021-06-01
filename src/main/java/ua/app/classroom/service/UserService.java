package ua.app.classroom.service;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import ua.app.classroom.db.UserMap;
import ua.app.classroom.entity.User;
import ua.app.classroom.util.ErrorMessage;
import ua.app.classroom.websocket.WebSocket;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collection;

@Named
@SessionScoped
public class UserService implements Serializable {

    private static final Logger LOG = Logger.getLogger(UserService.class);

    @Inject
    private UserMap userMap;

    @Inject
    private WebSocket webSocket;

    private User user = new User();

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

    public Collection<User> getUserList() {
        return userMap.getUserList();
    }

    public void setUserMap(UserMap userMap) {
        this.userMap = userMap;
    }
}
