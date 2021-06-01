package ua.app.classroom.security;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.app.classroom.db.UserDB;
import ua.app.classroom.entity.User;

@Service
public class SecurityUserDetailsService implements UserDetailsService {

    private static final Logger LOG = Logger.getLogger(SecurityUserDetailsService.class);

    @Autowired
    private UserDB userDB;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOG.info("Start loadUserByUsername method");
        User user = userDB.userIsExist(username);
        if (user == null) {
            LOG.info("User not found");
            throw new UsernameNotFoundException("User not found.");
        }
        return user;
    }
}
