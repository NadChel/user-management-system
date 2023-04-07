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

import javax.sql.DataSource;

@Configuration
@Import(WebMvcConfig.class)
@EnableWebSecurity
public class WebSecurityConfig {
    private final DataSource dataSource;

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
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/login");
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
        userDetailsManager.setUsersByUsernameQuery("SELECT username, password, enabled FROM users_db.users where username = ?");
        userDetailsManager.setAuthoritiesByUsernameQuery("SELECT username, role FROM users_db.user_role WHERE username = ?");

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsManager);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}