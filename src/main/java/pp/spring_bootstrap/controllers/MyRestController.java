package pp.spring_bootstrap.controllers;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pp.spring_bootstrap.models.User;
import pp.spring_bootstrap.service.UserRoleService;

import java.util.List;

@RestController
public class MyRestController {
    private final UserRoleService service;

    private final PasswordEncoder passwordEncoder;

    public MyRestController(UserRoleService employeeService, PasswordEncoder passwordEncoder) {
        this.service = employeeService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users")
    public List<User> showAllEmployees() {
        return service.getAll();
    }

    @PatchMapping("/users/{username}")
    public void disableEnableUserByUsername(@PathVariable String username,
                                      @RequestHeader String patch_type) {
        if (patch_type.equals("disable")) {
            service.disableUserByUsername(username);
        } else if (patch_type.equals("enable")) {
            service.enableUserByUsername(username);
        }
    }

    @PostMapping("/users")
    public User addEmployee(@RequestBody User user) {
        System.out.println("user in the post handler: " + user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        service.save(user);
        return user;
    }

    @PutMapping("/users")
    public User updateEmployee(@RequestBody User user, @RequestHeader String password_change) {
        System.out.println("user in the put handler: " + user);
        System.out.println("passwordChange: " + password_change);
        System.out.println("Boolean.parseBoolean(passwordChange): " + Boolean.parseBoolean(password_change));
        if (Boolean.parseBoolean(password_change)) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        service.save(user);
        return user;
    }

    @DeleteMapping("/users/{username}")
    public void deleteUserByUsername(@PathVariable String username) {
        service.deleteUserByUsername(username);
    }
}
