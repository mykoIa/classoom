package ua.app.classroom.bean;

import ua.app.classroom.db.UserDB;
import ua.app.classroom.model.entity.User;
import ua.app.classroom.util.SendMessage;
import ua.app.classroom.util.VerifyLoginAndPassword;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class EditBean implements Serializable {

    private static final String REDIRECT_AFTER_SAVE = "userList?faces-redirect=true";
    private static final String REDIRECT_EDIT = "edit?faces-redirect=true";

    private User user = new User();
    private String fullName;
    private boolean roleAdmin;

    public String edit(User user) {
        setUser(user);
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

    public boolean isRoleAdmin() {
        return roleAdmin;
    }

    public void setRoleAdmin(boolean roleAdmin) {
        this.roleAdmin = roleAdmin;
    }
}
