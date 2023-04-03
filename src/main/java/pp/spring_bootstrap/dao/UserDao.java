package pp.spring_bootstrap.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import pp.spring_bootstrap.models.User;

public interface UserDao extends JpaRepository<User, Long> {
    User findByUsername(String username);

    void deleteByUsername(String username);
}
