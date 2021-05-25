package ua.app.classroom.security;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;

public class MyDaoAuthenticationProvider extends DaoAuthenticationProvider {

    private static final Logger LOG = Logger.getLogger(MyDaoAuthenticationProvider.class);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        LOG.info("Start MyDaoAuthenticationProvider class");
        Authentication authenticate = super.authenticate(authentication);
        if (authenticate != null) {
            LOG.info("Do something");
        }
        return authenticate;
    }

    @Override
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        super.setUserDetailsService(userDetailsService);
    }
}
