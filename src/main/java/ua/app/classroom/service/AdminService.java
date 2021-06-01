package ua.app.classroom.service;

import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoader;
import ua.app.classroom.db.UserDB;
import ua.app.classroom.entity.User;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Named
@SessionScoped
public class AdminService implements Serializable {

    private static final Logger LOG = Logger.getLogger(AdminService.class);
    private final UserDB userDB = (UserDB) Objects.requireNonNull(ContextLoader.getCurrentWebApplicationContext()).getBean("userDB");

    private List<User> userList = new ArrayList<>();


    public List<User> getUserList() {
        return userList;
    }

    public void setUserList() {
        userList = userDB.getUserList();
    }
}
