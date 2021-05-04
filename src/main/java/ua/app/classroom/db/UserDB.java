package ua.app.classroom.db;

import ua.app.classroom.model.User;

import javax.enterprise.context.ApplicationScoped;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class UserDB {

    private final Map<String, User> userMap = new ConcurrentHashMap<>();

    public boolean isFullNameInUse(String fullName) {
        return userMap.containsKey(fullName);
    }

    public void addUser(User user) {
        userMap.put(user.getFullName(), user);
    }

    public void removeUser(User user) {
        userMap.remove(user.getFullName());
    }

    public Collection<User> getUserList() {
        return userMap.values();
    }

}
