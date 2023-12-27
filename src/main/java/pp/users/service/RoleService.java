package pp.users.service;

import pp.users.model.Role;

import java.util.Set;

public interface RoleService {
    Role findRoleByName(String name);

    Set<Role> findAdminRoleSet();

}
