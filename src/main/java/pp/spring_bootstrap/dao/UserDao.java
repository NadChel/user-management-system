package pp.spring_bootstrap.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import pp.spring_bootstrap.models.User;

import java.util.List;

public interface UserDao extends JpaRepository<User, Long> {
    List<User> findAllByUsernameNot(String username);
    User findByUsername(String username);

    void deleteByUsername(String username);
}
