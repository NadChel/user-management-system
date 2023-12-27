package pp.users.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pp.users.model.User;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserDaoTest {
    @Autowired
    UserDao userDao;
    String MICKEY = "mickey";
    String DAISY = "daisy";

    @BeforeEach
    void addSampleUsers() {
        User mickey = new User();
        mickey.setUsername(MICKEY);
        mickey.setPassword("password");
        mickey.setName("Mickey");
        mickey.setLastName("Mouse");
        mickey.setDepartment("IT");
        mickey.setAge((byte) 100);

        User daisy = new User();
        daisy.setUsername(DAISY);
        daisy.setPassword("password");
        daisy.setName("Daisy");
        daisy.setLastName("Duck");
        daisy.setDepartment("HR");
        daisy.setAge((byte) 100);

        userDao.saveAll(List.of(mickey, daisy));
    }

    @AfterEach
    void clearUsers() {
        userDao.deleteAll();
    }

    @Test
    void findAllExcept() {
        var usernames = userDao.findAllExcept(MICKEY).stream()
                .map(User::getUsername)
                .toList();
        assertThat(usernames.size()).isEqualTo(1);
        assertThat(usernames).asList().contains(DAISY);
    }

    @Test
    void findByUsername() {
        assertThat(userDao.findByUsername(MICKEY))
                .extracting(User::getUsername)
                .isEqualTo(MICKEY);
    }

    @Test
    void deleteByUsername() {
        userDao.deleteByUsername(MICKEY);
        var usernames = userDao.findAll().stream()
                .map(User::getUsername)
                .toList();
        assertThat(usernames).asList().doesNotContain(MICKEY);
    }
}