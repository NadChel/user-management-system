package pp.users.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pp.users.model.Role;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class RoleDaoTest {
    @Autowired
    RoleDao roleDao;
    static final String USER = "USER";
    static final String ADMIN = "ADMIN";

    @BeforeEach
    void addSampleRows() {
        roleDao.saveAll(List.of(new Role(USER), new Role(ADMIN)));
    }

    @AfterEach
    void clearRows() {
        roleDao.deleteAll();
    }

    @Test
    void findByAuthority() {
        assertThat(roleDao.findByAuthority(USER))
                .extracting(Role::getAuthority)
                .isEqualTo(USER);
    }

    @Test
    void findByAuthorityOrAuthority() {
        List<String> roleStrings = roleDao.findByAuthorityOrAuthority("USER", "ADMIN")
                .stream()
                .map(Role::getAuthority)
                .toList();
        assertThat(roleStrings.size()).isEqualTo(2);
        assertThat(roleStrings).asList().contains(USER, ADMIN);
    }
}