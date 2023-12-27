package pp.users.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pp.users.model.User;

import java.util.List;

public interface UserDao extends JpaRepository<User, Long> {
    @Query(value = """
            SELECT u
            FROM User u LEFT JOIN FETCH u.authorities
            WHERE u.username != :username
            """)
    List<User> findAllExcept(String username);

    @Query(value = """
            SELECT u
            FROM User u LEFT JOIN FETCH u.authorities
            WHERE u.username = :username
            """)
    User findByUsername(String username);

    void deleteByUsername(String username);
}
