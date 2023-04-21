package pp.spring_bootstrap.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pp.spring_bootstrap.models.Role;
import pp.spring_bootstrap.models.User;
import pp.spring_bootstrap.service.RoleService;
import pp.spring_bootstrap.service.UserService;

@Controller
public class MyController {
    private final UserService userService;

    private final RoleService roleService;

    public MyController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        User loggedUser = userService.getLoggedUser(authentication);
        String loggedUserUsername = loggedUser.getUsername();
        Role userRole = roleService.getRoleByName("USER");

        model.addAttribute("loggedUser", loggedUser)
                .addAttribute("users", userService.getAllExceptLoggedUser(loggedUserUsername))
                .addAttribute("newUser", new User(userRole))
                .addAttribute("adminRoleSet", roleService.getAdminRoleSet());
        return "home-page";
    }
}
