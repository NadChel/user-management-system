package pp.spring_bootstrap.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import pp.spring_bootstrap.dao.UserDao;
import pp.spring_bootstrap.models.User;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @InjectMocks
    UserServiceImpl userService;
    @Mock
    UserDao userDao;
    static final String MICKEY;
    static final User MICKEY_USER;
    static final String DAISY;
    static final User DAISY_USER;

    static {
        MICKEY = "mickey";
        MICKEY_USER = new User();
        MICKEY_USER.setUsername(MICKEY);
        MICKEY_USER.setPassword("password");
        MICKEY_USER.setName("Mickey");
        MICKEY_USER.setLastName("Mouse");
        MICKEY_USER.setDepartment("IT");
        MICKEY_USER.setAge((byte) 100);

        DAISY = "daisy";
        DAISY_USER = new User();
        DAISY_USER.setUsername(DAISY);
        DAISY_USER.setPassword("password");
        DAISY_USER.setName("Daisy");
        DAISY_USER.setLastName("Duck");
        DAISY_USER.setDepartment("HR");
        DAISY_USER.setAge((byte) 100);
    }

    @Test
    void findAllExceptLoggedUser() {
        when(userDao.findAllExcept(MICKEY)).thenReturn(List.of(DAISY_USER));
        var usernames = userService.findAllExceptLoggedUser(MICKEY).stream().map(User::getUsername).toList();
        assertThat(usernames).asList().doesNotContain(MICKEY);
        verify(userDao, times(1)).findAllExcept(MICKEY);
    }

    @Test
    void findLoggedUser() {
        when(userDao.findByUsername(MICKEY)).thenReturn(MICKEY_USER);
        var authenticationMock = mock(Authentication.class);
        when(authenticationMock.getPrincipal()).thenReturn(MICKEY_USER);
        assertThat(userService.findLoggedUser(authenticationMock))
                .extracting(User::getUsername).isEqualTo(MICKEY);
        verify(userDao, times(1)).findByUsername(MICKEY);
    }

    @Test
    void save() {
        userService.save(MICKEY_USER);
        verify(userDao, times(1)).save(MICKEY_USER);
    }

    @Test
    void disableUserByUsername() {
        MICKEY_USER.setEnabledByte((byte) 1);
        when(userDao.findByUsername(MICKEY)).thenReturn(MICKEY_USER);
        userService.disableUserByUsername(MICKEY);
        assertThat(MICKEY_USER.getEnabledByte()).isEqualTo((byte) 0);
        verify(userDao, times(1)).findByUsername(MICKEY);
    }

    @Test
    void enableUserByUsername() {
        MICKEY_USER.setEnabledByte((byte) 0);
        when(userDao.findByUsername(MICKEY)).thenReturn(MICKEY_USER);
        userService.enableUserByUsername(MICKEY);
        assertThat(MICKEY_USER.getEnabledByte()).isEqualTo((byte) 1);
        verify(userDao, times(1)).findByUsername(MICKEY);
    }

    @Test
    void deleteUserByUsername() {
        userService.deleteUserByUsername(MICKEY);
        verify(userDao, times(1)).deleteByUsername(MICKEY);
    }
}