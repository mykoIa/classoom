package ua.app.classroom.service;

import org.apache.log4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ua.app.classroom.db.UserDB;
import ua.app.classroom.model.User;

import javax.faces.annotation.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.security.enterprise.authentication.mechanism.http.AutoApplySession;

@AutoApplySession
public class SecurityUserDetailsService implements UserDetailsService {

    private static final Logger LOG = Logger.getLogger(SecurityUserDetailsService.class);
    @ManagedProperty("#{facesContext}")
    FacesContext faces;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOG.info("Start loadUserByUsername method");
        UserDB userDB = new UserDB();   //del
        User user = userDB.findUserByName(username);
        if (user == null) {
            LOG.info("User not found");
            throw new UsernameNotFoundException("User not found.");
        }
        return user;
    }
}