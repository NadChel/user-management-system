package pp.spring_bootstrap.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
        model.addAttribute("loggedUser", userService.getLoggedUser(authentication))
                .addAttribute("users", userService.getAllExceptLoggedUser(authentication))
                .addAttribute("newUser", new User(roleService.getRoleByName("USER")))
                .addAttribute("adminRoleSet", roleService.getAdminRoleSet());
        return "home-page";
    }
}
