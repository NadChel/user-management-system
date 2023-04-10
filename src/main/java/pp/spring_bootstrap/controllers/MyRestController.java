package pp.spring_bootstrap.controllers;

import org.springframework.web.bind.annotation.*;
import pp.spring_bootstrap.models.User;
import pp.spring_bootstrap.service.UserRoleService;

import java.util.List;

@RestController
public class MyRestController {
    private final UserRoleService service;

    public MyRestController(UserRoleService employeeService) {
        this.service = employeeService;
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
        service.save(user);
        return user;
    }

    @PutMapping("/users")
    public User updateEmployee(@RequestBody User user) {
        service.save(user);
        return user;
    }

    @DeleteMapping("/users/{username}")
    public void deleteUserByUsername(@PathVariable String username) {
        service.deleteUserByUsername(username);
    }
}
