package ua.app.classroom.bean;

import org.springframework.security.core.context.SecurityContextHolder;
import ua.app.classroom.model.entity.User;
import ua.app.classroom.security.CustomDetailService;
import ua.app.classroom.websocket.WebSocket;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class MenuBean implements Serializable {

    @Inject
    private WebSocket webSocket;

    private User user = new User();

    {
        CustomDetailService customUserDetail = (CustomDetailService) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        setUser(customUserDetail.getUser());
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
}
