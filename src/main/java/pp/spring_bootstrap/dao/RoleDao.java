package pp.spring_bootstrap.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import pp.spring_bootstrap.models.Role;

public interface RoleDao extends JpaRepository<Role, Long> {
    Role findByAuthority(String authority);
}
