package pp.users.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import pp.users.model.Role;

import java.util.List;

public interface RoleDao extends JpaRepository<Role, Long> {
    Role findByAuthority(String authority);

    List<Role> findByAuthorityOrAuthority(String auth1, String auth2);
}
