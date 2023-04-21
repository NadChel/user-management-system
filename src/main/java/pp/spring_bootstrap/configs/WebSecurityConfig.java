package pp.spring_bootstrap.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.sql.DataSource;

@Configuration
@Import(WebMvcConfig.class)
@EnableWebSecurity
public class WebSecurityConfig {
    private final DataSource dataSource;

    private static final String USERS_BY_USERNAME_QUERY = """
            SELECT username, password, enabled
            FROM users where username = ?
            """;

    private static final String AUTHORITIES_BY_USERNAME_QUERY = """
            SELECT users.username, roles.role
            FROM user_role
            JOIN users ON user_role.user_id = users.id
            JOIN roles ON user_role.role_id = roles.id
            WHERE users.username = ?
            """;

    public WebSecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
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
    public AuthenticationProvider authenticationProvider() {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
        userDetailsManager.setUsersByUsernameQuery(USERS_BY_USERNAME_QUERY);
        userDetailsManager.setAuthoritiesByUsernameQuery(AUTHORITIES_BY_USERNAME_QUERY);

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsManager);
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