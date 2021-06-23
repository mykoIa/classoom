package ua.app.classroom.bean;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import ua.app.classroom.db.UserMap;
import ua.app.classroom.model.entity.User;
import ua.app.classroom.security.CustomDetailService;
import ua.app.classroom.util.SendMessage;
import ua.app.classroom.websocket.WebSocket;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class LoginLogoutBean implements Serializable {

    private static final Logger LOG = Logger.getLogger(LoginLogoutBean.class);
    private static final String REDIRECT_LOGIN = "/login?faces-redirect=true";
    private static final String REDIRECT_MEMBERS = "/secure/user/members?faces-redirect=true";

    @Inject
    private UserMap userMap;

    @Inject
    private WebSocket webSocket;

    private User user = new User();

    public String logout() {
        userMap.removeUser(user);
        webSocket.userDisconnected(user.getFullName());
        LOG.trace("Method logOut completed successfully");
        return REDIRECT_LOGIN;
    }

    public String login() {
        CustomDetailService customUserDetail = (CustomDetailService) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        setUser(customUserDetail.getUser());
        userMap.addUserToMap(user);
        webSocket.userConnected(user.getFullName());
        LOG.trace("Method authorizeUser completed successfully");
        return REDIRECT_MEMBERS;
    }

    public String errorLogin() {
        SendMessage.sendMessageWhenAuthorizeFailed();
        return REDIRECT_LOGIN;
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
}
