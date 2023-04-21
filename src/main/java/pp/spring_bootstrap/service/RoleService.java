package pp.spring_bootstrap.service;

import pp.spring_bootstrap.models.Role;

import java.util.Set;

public interface RoleService {
    Role getRoleByName(String name);

    Set<Role> getAdminRoleSet();

}
