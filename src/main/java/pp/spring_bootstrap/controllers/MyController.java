package pp.spring_bootstrap.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pp.spring_bootstrap.models.User;
import pp.spring_bootstrap.service.UserRoleService;

@Controller
public class MyController {
    private final UserRoleService service;

    private final PasswordEncoder passwordEncoder;

    public MyController(UserRoleService service, PasswordEncoder passwordEncoder) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        model.addAttribute("loggedUser", service.getLoggedUser(authentication))
                .addAttribute("users", service.getAllExceptLoggedUser(authentication))
                .addAttribute("newUser", new User())
                .addAttribute("adminRoleSet", service.getAdminRoleSet());
        return "home-page";
    }

    @GetMapping("/admin/disable-user")
    public String disableUser(@RequestParam String username) {
        service.disableUserByUsername(username);
        return "redirect:/";
    }

    @GetMapping("/admin/enable-user")
    public String enableUser(@RequestParam String username) {
        service.enableUserByUsername(username);
        return "redirect:/";
    }

    @GetMapping("/admin/delete-user")
    public String deleteUser(@RequestParam String username) {
        service.deleteUserByUsername(username);
        return "redirect:/";
    }

    @PostMapping("/save-user")
    public String saveUser(@ModelAttribute User user,
                           @RequestParam(defaultValue = "false") String passwordChange) {
        if (Boolean.parseBoolean(passwordChange)) {
            encodePassword(user);
        }
        service.save(user);
        return "redirect:/";
    }

    private void encodePassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }
}
