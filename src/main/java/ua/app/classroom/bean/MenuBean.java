package ua.app.classroom.bean;

import ua.app.classroom.db.repository.CurrentUser;
import ua.app.classroom.model.entity.User;
import ua.app.classroom.websocket.WebSocket;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class MenuBean implements Serializable {

    @Inject
    private WebSocket webSocket;

    private User user = CurrentUser.getUser();

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
