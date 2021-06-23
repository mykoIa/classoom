package ua.app.classroom.service;

import ua.app.classroom.db.UserDB;
import ua.app.classroom.model.entity.User;
import ua.app.classroom.util.SendMessage;
import ua.app.classroom.util.VerifyLoginAndPassword;
import ua.app.classroom.websocket.WebSocketForAdmin;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Named
@SessionScoped
public class AdminService implements Serializable {

    private static final String REDIRECT_AFTER_SAVE = "userList";
    private static final String REDIRECT_EDIT = "edit";

    @Inject
    private WebSocketForAdmin webSocket;

    private List<User> userList;
    private User user = new User();
    private String fullName;
    private boolean roleAdmin;

    public void deleteUser(User user) {
        UserDB.deleteUser(user);
        webSocket.userDeleted(user.getFullName());
    }

    public String edit(User user) {
        this.user = user;
        setFullName(user.getFullName());
        if (user.getRole().equals("ROLE_ADMIN")) {
            setRoleAdmin(true);
        }
        return REDIRECT_EDIT;
    }

    public String save() {
        if (VerifyLoginAndPassword.loginIsEmpty(fullName)) {
            return "";
        }
        if (isRoleAdmin()) {
            user.setRole("ROLE_ADMIN");
        } else {
            user.setRole("ROLE_USER");
        }
        if (user.getFullName().equals(fullName) || !UserDB.userIsExist(fullName)) {
            UserDB.updateUser(user, fullName);
        } else {
            SendMessage.loginIsAlreadyTaken();
            return "";
        }
        setRoleAdmin(false);
        return REDIRECT_AFTER_SAVE;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Collection<User> getUserList() {
        return UserDB.getUserList();
    }

    public boolean isRoleAdmin() {
        return roleAdmin;
    }

    public void setRoleAdmin(boolean roleAdmin) {
        this.roleAdmin = roleAdmin;
    }
}
