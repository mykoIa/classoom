package ua.app.classroom.service;

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
public class RegistrationBean implements Serializable {

    public static final String LOGIN_FACES_REDIRECT_TRUE = "login?faces-redirect=true";
    private static final Logger LOG = Logger.getLogger(UserService.class);

    User user = new User();
    private String password;

    public String registration() {
        user.setFullName(user.getFullName().trim());
        if (VerifyLoginAndPassword.loginOrPasswordIsEmpty(user, password)) {
            return "";
        }
        if (UserDB.userIsExist(user.getFullName())) {
            LOG.trace("Login is already taken");
            SendMessage.loginIsAlreadyTaken();
            return "";
        }
        UserDB.addUserToDB(user, password);
        resetFields();
        LOG.trace("Method registrationUser completed successfully");
        return LOGIN_FACES_REDIRECT_TRUE;
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
}
