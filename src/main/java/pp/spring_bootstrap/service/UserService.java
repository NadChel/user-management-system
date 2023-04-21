package pp.spring_bootstrap.service;


import org.springframework.security.core.Authentication;
import pp.spring_bootstrap.models.User;

import java.util.List;

public interface UserService {

    List<User> getAllExceptLoggedUser(String username);

    User getLoggedUser(Authentication authentication);

    void save(User user);

    void disableUserByUsername(String username);

    void enableUserByUsername(String username);

    void deleteUserByUsername(String username);

}
