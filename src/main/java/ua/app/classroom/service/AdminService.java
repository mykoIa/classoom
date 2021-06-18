package ua.app.classroom.service;

import org.springframework.web.context.ContextLoader;
import ua.app.classroom.db.UserDB;
import ua.app.classroom.model.entity.User;
import ua.app.classroom.util.SendMessage;
import ua.app.classroom.websocket.WebSocketForAdmin;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Named
@SessionScoped
public class AdminService implements Serializable {

    public static final String REDIRECT_AFTER_SAVE = "user-list?faces-redirect=true";
    private final UserDB userDB = (UserDB) Objects.requireNonNull(ContextLoader.getCurrentWebApplicationContext()).getBean("userDB");

    @Inject
    private WebSocketForAdmin webSocket;

    private List<User> userList;
    private User user = new User();
    private String fullName;
    private boolean roleAdmin;

    public void deleteUser(User user) {
        userDB.deleteUser(user);
        webSocket.userDeleted(user.getFullName());
    }

    public String edit(User user) {
        this.user = user;
        fullName = user.getFullName();
        if (user.getRole().equals("ROLE_ADMIN")) {
            setRoleAdmin(true);
        }
        return "edit?faces-redirect=true";
    }

    public String save() {
        if (SendMessage.chekLogin(fullName) || SendMessage.checkRole(user)) {
            return "";
        }
        if (roleAdmin) {
            user.setRole("ROLE_ADMIN");
        } else {
            user.setRole("ROLE_USER");
        }
        if (user.getFullName().equals(fullName) || !userDB.userIsExistTest(fullName)) {
            userDB.updateUser(user, fullName);
        } else {
            SendMessage.loginIsAlreadyTaken();
            return "";
        }
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
        return userDB.getUserList();
    }

    public boolean isRoleAdmin() {
        return roleAdmin;
    }

    public void setRoleAdmin(boolean roleAdmin) {
        this.roleAdmin = roleAdmin;
    }
}
