package ua.app.classroom.security;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import ua.app.classroom.model.User;
import ua.app.classroom.service.UserService;

public class MyDaoAuthenticationProvider extends DaoAuthenticationProvider {

    private static final Logger LOG = Logger.getLogger(MyDaoAuthenticationProvider.class);

    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        LOG.info("Start MyDaoAuthenticationProvider class");
        Authentication authenticate = super.authenticate(authentication);
        User user = (User) authenticate.getPrincipal();
        userService.authorizeUser(user);
//        userMap.addUserToMap(user);
//        webSocket.userConnected(user.getFullName());
        return authenticate;
    }

    @Override
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        super.setUserDetailsService(userDetailsService);

    }
}
