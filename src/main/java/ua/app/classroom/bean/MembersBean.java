package ua.app.classroom.bean;

import ua.app.classroom.db.UserMap;
import ua.app.classroom.model.entity.User;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collection;

@Named
@ViewScoped
public class MembersBean implements Serializable {

    @Inject
    private UserMap userMap;

    public Collection<User> getUserList() {
        return userMap.getUserList();
    }

    public void setUserMap(UserMap userMap) {
        this.userMap = userMap;
    }
}
