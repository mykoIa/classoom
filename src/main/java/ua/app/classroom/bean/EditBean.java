package ua.app.classroom.bean;

import ua.app.classroom.db.UserDB;
import ua.app.classroom.db.repository.SelectedUser;
import ua.app.classroom.util.SendMessage;
import ua.app.classroom.util.VerifyLoginAndPassword;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@RequestScoped
public class EditBean implements Serializable {

    private static final String REDIRECT_AFTER_SAVE = "userList?faces-redirect=true";

    private String fullName = SelectedUser.getFullName();
    private boolean roleAdmin = SelectedUser.isRoleAdmin();

    public String save() {
        if (VerifyLoginAndPassword.loginIsEmpty(SelectedUser.getFullName())) {
            return "";
        }
        if (isRoleAdmin()) {
            SelectedUser.getUser().setRole("ROLE_ADMIN");
        } else {
            SelectedUser.getUser().setRole("ROLE_USER");
        }
        if (SelectedUser.getUser().getFullName().equals(SelectedUser.getFullName()) || !UserDB.userIsExist(SelectedUser.getFullName())) {
            UserDB.updateUser(SelectedUser.getUser(), fullName);
        } else {
            SendMessage.loginIsAlreadyTaken();
            return "";
        }
        SelectedUser.setRoleAdmin(false);
        SelectedUser.clear();
        return REDIRECT_AFTER_SAVE;
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
