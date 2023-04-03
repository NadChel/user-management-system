package pp.spring_bootstrap.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String home(Authentication authentication) {
        return isAdmin(authentication) ?
                "redirect:/admin" :
                "redirect:/user";
    }

    @GetMapping("/admin")
    public String admin(Model model, Authentication authentication) {
        model.addAttribute("users", service.getAllExceptLoggedUser(authentication))
                .addAttribute("loggedUser", service.getLoggedUser(authentication))
                .addAttribute("newUser", new User())
                .addAttribute("adminRoleSet", service.getAdminRoleSet());
        return "admin";
    }

    @GetMapping("/user")
    public String user(Model model, Authentication authentication) {
        model.addAttribute("loggedUser", service.getLoggedUser(authentication));
        return "user";
    }

    private String getUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"));
    }

    private void encodePassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }
}
