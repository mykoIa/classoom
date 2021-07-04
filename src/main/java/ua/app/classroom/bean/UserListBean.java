package ua.app.classroom.bean;

import ua.app.classroom.db.UserDB;
import ua.app.classroom.db.repository.SelectedUser;
import ua.app.classroom.model.entity.User;
import ua.app.classroom.websocket.WebSocketForAdmin;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Named
@ViewScoped
public class UserListBean implements Serializable {

    private static final String REDIRECT_EDIT = "edit?faces-redirect=true";

    @Inject
    private WebSocketForAdmin webSocket;

    private List<User> userList;

    public String edit(User user) {
        SelectedUser.setUser(user);
        SelectedUser.setFullName(SelectedUser.getUser().getFullName());
        if (SelectedUser.getUser().getRole().equals("ROLE_ADMIN")) {
            SelectedUser.setRoleAdmin(true);
        }
        return REDIRECT_EDIT;
    }

    public void deleteUser(User user) {
        UserDB.deleteUser(user);
        webSocket.userDeleted(user.getFullName());
    }

    public Collection<User> getUserList() {
        return UserDB.getUserList();
    }
}
