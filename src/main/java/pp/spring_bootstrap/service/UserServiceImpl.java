package pp.spring_bootstrap.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pp.spring_bootstrap.dao.UserDao;
import pp.spring_bootstrap.models.User;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserDao userDao;


    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<User> getAllExceptLoggedUser(String username) {
        return userDao.findAllByUsernameNot(username);
    }

    @Override
    public User getLoggedUser(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String loggedUserName = userDetails.getUsername();
        return userDao.findByUsername(loggedUserName);
    }

    @Override
    @Transactional
    public void save(User user) {
        userDao.save(user);
    }

    @Override
    @Transactional
    public void disableUserByUsername(String username) {
        User user = userDao.findByUsername(username);
        user.setEnabledByte((byte) 0);
    }

    @Override
    @Transactional
    public void enableUserByUsername(String username) {
        User user = userDao.findByUsername(username);
        user.setEnabledByte((byte) 1);
    }

    @Override
    @Transactional
    public void deleteUserByUsername(String username) {
        userDao.deleteByUsername(username);
    }

}
