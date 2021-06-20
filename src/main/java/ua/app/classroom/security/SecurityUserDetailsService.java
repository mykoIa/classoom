package ua.app.classroom.security;

import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.app.classroom.db.UserDB;
import ua.app.classroom.model.entity.User;

import java.util.HashSet;
import java.util.Set;

@Service
public class SecurityUserDetailsService implements UserDetailsService {

    private static final Logger LOG = Logger.getLogger(SecurityUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOG.info("Start loadUserByUsername method");
        User user = UserDB.userIsExist(username);
        if (user == null) {
            LOG.info("User not found");
            throw new UsernameNotFoundException("User not found.");
        }
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));

        CustomDetailService customUserDetail = new CustomDetailService();
        customUserDetail.setUser(user);
        customUserDetail.setAuthorities(authorities);

        return customUserDetail;
    }
}
