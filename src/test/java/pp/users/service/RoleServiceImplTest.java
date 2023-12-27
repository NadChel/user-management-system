package pp.users.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pp.users.dao.RoleDao;
import pp.users.model.Role;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {
    @InjectMocks
    RoleServiceImpl roleService;
    @Mock
    RoleDao roleDao;
    static final String USER = "USER";
    static final String ADMIN = "ADMIN";

    @Test
    void findRoleByName() {
        when(roleDao.findByAuthority(USER)).thenReturn(new Role(USER));
        assertThat(roleService.findRoleByName(USER)).extracting(Role::getAuthority).isEqualTo(USER);
        verify(roleDao, times(1)).findByAuthority(USER);
    }

    @Test
    void findAdminRoleSet() {
        when(roleDao.findByAuthorityOrAuthority(USER, ADMIN))
                .thenReturn(List.of(new Role(USER), new Role(ADMIN)));
        var adminRoles = roleService.findAdminRoleSet().stream()
                .map(Role::getAuthority)
                .toList();
        assertThat(adminRoles.size()).isEqualTo(2);
        assertThat(adminRoles).asList().contains(ADMIN, USER);
        verify(roleDao, times(1)).findByAuthorityOrAuthority(USER, ADMIN);
    }
}