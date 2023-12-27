package pp.users.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pp.users.dao.RoleDao;
import pp.users.model.Role;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {
    private final RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public Role findRoleByName(String name) {
        return roleDao.findByAuthority(name);
    }

    @Override
    public Set<Role> findAdminRoleSet() {
        return new HashSet<>(roleDao.findByAuthorityOrAuthority("USER", "ADMIN"));
    }
}
