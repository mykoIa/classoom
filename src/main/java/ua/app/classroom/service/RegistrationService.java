package ua.app.classroom.service;

import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoader;
import ua.app.classroom.db.UserDB;
import ua.app.classroom.entity.User;
import ua.app.classroom.util.ErrorMessage;

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

    public String singIn() {
        UserDB userDB = (UserDB) Objects.requireNonNull(ContextLoader.getCurrentWebApplicationContext()).getBean("userDB");
        user.setFullName(user.getFullName().trim());
        if (ErrorMessage.loginOrPasswordIsEmpty(user, password)) {
            return "";
        }
        if (userDB.userIsExist(user)) {
            LOG.trace("Login is already taken");
            ErrorMessage.setMessage("This login is already taken");
            return "";
        }
        userDB.addUserToDB(user, password);
        LOG.trace("Method registrationUser completed successfully");
        return LOGIN_FACES_REDIRECT_TRUE;
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
