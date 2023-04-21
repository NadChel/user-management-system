package pp.spring_bootstrap.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pp.spring_bootstrap.dao.RoleDao;
import pp.spring_bootstrap.models.Role;

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
    public Role getRoleByName(String name) {
        return roleDao.findByAuthority(name);
    }

    @Override
    public Set<Role> getAdminRoleSet() {
        return new HashSet<>(roleDao.findByAuthorityOrAuthority("USER", "ADMIN"));
    }

}
