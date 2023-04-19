package pp.spring_bootstrap.service;


import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import pp.spring_bootstrap.models.Role;
import pp.spring_bootstrap.models.User;

import java.util.List;
import java.util.Set;

public interface UserRoleService {

    List<User> getAllExceptLoggedUser(Authentication authentication);

    User getLoggedUser(Authentication authentication);

    void save(User user);

    void disableUserByUsername(String username);

    void enableUserByUsername(String username);

    void deleteUserByUsername(String username);

    Role getRoleByName(String name);

    Set<Role> getAdminRoleSet();

    void encodePassword(User user, PasswordEncoder passwordEncoder);
}
