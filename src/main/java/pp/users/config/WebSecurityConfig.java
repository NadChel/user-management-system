package pp.users.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import pp.users.dao.UserDao;
import pp.users.model.User;

@Configuration
@Import(WebMvcConfig.class)
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserDao userDao;

    public WebSecurityConfig(UserDao userDao) {
        this.userDao = userDao;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers("/user/**").hasAuthority("USER")
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
                .and().formLogin()
                .successHandler(successHandler())
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/login");
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            User user = userDao.findByUsername(username);
            if (user == null) {
                String message = String.format("No user with username %s is found in the database", username);
                throw new UsernameNotFoundException(message);
            }
            return user;
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (httpServletRequest, httpServletResponse, authentication) ->
                httpServletResponse.sendRedirect("/");
    }
}