package ua.app.classroom.service;

import ua.app.classroom.db.UserDB;
import ua.app.classroom.model.entity.User;
import ua.app.classroom.websocket.WebSocketForAdmin;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Named
@SessionScoped
public class UserListBean implements Serializable {

    @Inject
    private WebSocketForAdmin webSocket;

    private List<User> userList;

    public void deleteUser(User user) {
        UserDB.deleteUser(user);
        webSocket.userDeleted(user.getFullName());
    }

    public Collection<User> getUserList() {
        return UserDB.getUserList();
    }
}
