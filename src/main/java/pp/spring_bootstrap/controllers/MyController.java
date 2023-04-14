package pp.spring_bootstrap.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pp.spring_bootstrap.models.User;
import pp.spring_bootstrap.service.UserRoleService;

@Controller
public class MyController {
    private final UserRoleService service;

    public MyController(UserRoleService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        model.addAttribute("loggedUser", service.getLoggedUser(authentication))
                .addAttribute("users", service.getAllExceptLoggedUser(authentication))
                .addAttribute("newUser", new User(service.getRoleByName("USER")))
                .addAttribute("adminRoleSet", service.getAdminRoleSet());
        return "home-page";
    }
}
