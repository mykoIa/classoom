package ua.app.classroom.bean;

import org.apache.log4j.Logger;
import ua.app.classroom.db.UserDB;
import ua.app.classroom.model.entity.User;
import ua.app.classroom.util.SendMessage;
import ua.app.classroom.util.VerifyLoginAndPassword;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class AddUserBean implements Serializable {
    public static final String USER_LIST_REDIRECT_TRUE = "userList?faces-redirect=true";
    private static final Logger LOG = Logger.getLogger(LoginLogoutBean.class);

    User user = new User();
    private String password;
    private boolean roleAdmin;

    public String addUserToDB() {
        user.setFullName(user.getFullName().trim());
        if (VerifyLoginAndPassword.loginOrPasswordIsEmpty(user, password)) {
            return "";
        }
        if (UserDB.userIsExist(user.getFullName())) {
            LOG.trace("Login is already taken");
            SendMessage.loginIsAlreadyTaken();
            return "";
        }
        if (isRoleAdmin()) {
            user.setRole("ROLE_ADMIN");
        }
        UserDB.addUserToDB(user, password);
        resetFields();
        LOG.trace("Method registrationUser completed successfully");
        return USER_LIST_REDIRECT_TRUE;
    }

    private void resetFields() {
        clearUser();
        setPassword("");
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRoleAdmin() {
        return roleAdmin;
    }

    public void setRoleAdmin(boolean roleAdmin) {
        this.roleAdmin = roleAdmin;
    }
}

