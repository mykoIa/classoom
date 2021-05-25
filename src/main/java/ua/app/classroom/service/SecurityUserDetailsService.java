package ua.app.classroom.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.app.classroom.db.UserDB;
import ua.app.classroom.model.User;
import ua.app.classroom.security.MyDaoAuthenticationProvider;
import ua.app.classroom.websocket.WebSocket;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.authentication.mechanism.http.AutoApplySession;

@AutoApplySession
public class SecurityUserDetailsService implements UserDetailsService {

    private static final Logger LOG = Logger.getLogger(SecurityUserDetailsService.class);

    @Inject
    private WebSocket webSocket;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOG.info("Start loadUserByUsername method");
        UserDB userDB = new UserDB();
        User user = userDB.findUserByName(username);
        if (user == null) {
            LOG.info("User not found");
            throw new UsernameNotFoundException("User not found.");
        }
        LOG.info("name:" + user.getFullName() + "\npassword: " + user.getPassword());
        return user;
    }
}