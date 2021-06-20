package ua.app.classroom.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.app.classroom.security.CustomAuthenticationFailureHandler;
import ua.app.classroom.security.CustomAuthenticationSuccessHandler;
import ua.app.classroom.security.CustomLogoutSuccessHandler;
import ua.app.classroom.security.SecurityUserDetailsService;
import ua.app.classroom.service.AdminService;
import ua.app.classroom.service.RegistrationService;
import ua.app.classroom.service.UserService;
import ua.app.classroom.websocket.WebSocket;
import ua.app.classroom.websocket.WebSocketForAdmin;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = "ua.app.classroom.security")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityUserDetailsService securityUserDetailsService;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().
                antMatchers("/secure/user/**").authenticated().
                antMatchers("/login.xhtml", "/registration.xhtml").anonymous().
                antMatchers("/secure/admin/**").hasRole("ADMIN").
                and().formLogin().
                loginPage("/login.xhtml").
                loginProcessingUrl("/appLogin").
                usernameParameter("app_username").
                passwordParameter("app_password").
                successHandler(customAuthenticationSuccessHandler).
                failureHandler(customAuthenticationFailureHandler).
                and().logout().
                logoutUrl("/appLogout").
                logoutSuccessHandler(customLogoutSuccessHandler);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(securityUserDetailsService).passwordEncoder(passwordEncoder());
    }

}  
