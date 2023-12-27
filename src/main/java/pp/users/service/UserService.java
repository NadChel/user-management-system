package pp.users.service;


import org.springframework.security.core.Authentication;
import pp.users.model.User;

import java.util.List;

public interface UserService {

    List<User> findAllExceptLoggedUser(String username);

    User findLoggedUser(Authentication authentication);

    void save(User user);

    void disableUserByUsername(String username);

    void enableUserByUsername(String username);

    void deleteUserByUsername(String username);

}
