package ua.app.classroom.service;

import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoader;
import ua.app.classroom.db.UserDB;
import ua.app.classroom.model.entity.User;
import ua.app.classroom.util.SendMessage;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Objects;

@Named
@SessionScoped
public class RegistrationService implements Serializable {

    public static final String LOGIN_FACES_REDIRECT_TRUE = "login?faces-redirect=true";
    private static final Logger LOG = Logger.getLogger(UserService.class);

    User user = new User();
    private String password;
    private boolean roleAdmin;

    public String registration() {
        if (checkAndAddToDB()) {
            return "";
        }
        return LOGIN_FACES_REDIRECT_TRUE;
    }

    public void addUserToList() {
        if (!checkAndAddToDB()) {
            SendMessage.userAdded();
        }
    }

    private boolean checkAndAddToDB() {
        UserDB userDB = (UserDB) Objects.requireNonNull(ContextLoader.getCurrentWebApplicationContext()).getBean("userDB");
        user.setFullName(user.getFullName().trim());
        if (SendMessage.loginOrPasswordIsEmpty(user, password)) {
            return true;
        }
        if (userDB.userIsExist(user)) {
            LOG.trace("Login is already taken");
            SendMessage.loginIsAlreadyTaken();
            return true;
        }
        if (roleAdmin) {
            user.setRole("ADMIN");
        }
        userDB.addUserToDB(user, password);
        resetFields();
        LOG.trace("Method registrationUser completed successfully");
        return false;
    }

    private void resetFields() {
        roleAdmin = false;
        user.setFullName("");
        user.setRole("USER");
        password = "";
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
