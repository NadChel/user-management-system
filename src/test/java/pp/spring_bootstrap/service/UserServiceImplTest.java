package pp.spring_bootstrap.service;

import org.junit.jupiter.api.BeforeEach;
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
    String mickey;
    User mickeyUser;
    String daisy;
    User daisyUser;

    @BeforeEach
    void setUp() {
        mickey = "mickey";
        mickeyUser = new User();
        mickeyUser.setUsername(mickey);
        mickeyUser.setPassword("password");
        mickeyUser.setName("Mickey");
        mickeyUser.setLastName("Mouse");
        mickeyUser.setDepartment("IT");
        mickeyUser.setAge((byte) 100);

        daisy = "daisy";
        daisyUser = new User();
        daisyUser.setUsername(daisy);
        daisyUser.setPassword("password");
        daisyUser.setName("Daisy");
        daisyUser.setLastName("Duck");
        daisyUser.setDepartment("HR");
        daisyUser.setAge((byte) 100);
    }

    @Test
    void findAllExceptLoggedUser() {
        when(userDao.findAllExcept(mickey)).thenReturn(List.of(daisyUser));
        var usernames = userService.findAllExceptLoggedUser(mickey).stream().map(User::getUsername).toList();
        assertThat(usernames).asList().doesNotContain(mickey);
        verify(userDao, times(1)).findAllExcept(mickey);
    }

    @Test
    void findLoggedUser() {
        when(userDao.findByUsername(mickey)).thenReturn(mickeyUser);
        var authenticationMock = mock(Authentication.class);
        when(authenticationMock.getPrincipal()).thenReturn(mickeyUser);
        assertThat(userService.findLoggedUser(authenticationMock))
                .extracting(User::getUsername).isEqualTo(mickey);
        verify(userDao, times(1)).findByUsername(mickey);
    }

    @Test
    void save() {
        userService.save(mickeyUser);
        verify(userDao, times(1)).save(mickeyUser);
    }

    @Test
    void disableUserByUsername() {
        mickeyUser.setEnabledByte((byte) 1);
        when(userDao.findByUsername(mickey)).thenReturn(mickeyUser);
        userService.disableUserByUsername(mickey);
        assertThat(mickeyUser.getEnabledByte()).isEqualTo((byte) 0);
        verify(userDao, times(1)).findByUsername(mickey);
    }

    @Test
    void enableUserByUsername() {
        mickeyUser.setEnabledByte((byte) 0);
        when(userDao.findByUsername(mickey)).thenReturn(mickeyUser);
        userService.enableUserByUsername(mickey);
        assertThat(mickeyUser.getEnabledByte()).isEqualTo((byte) 1);
        verify(userDao, times(1)).findByUsername(mickey);
    }

    @Test
    void deleteUserByUsername() {
        userService.deleteUserByUsername(mickey);
        verify(userDao, times(1)).deleteByUsername(mickey);
    }
}