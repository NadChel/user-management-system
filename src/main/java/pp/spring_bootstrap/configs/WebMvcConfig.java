package pp.spring_bootstrap.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pp.spring_bootstrap.models.Role;
import pp.spring_bootstrap.service.UserRoleService;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final UserRoleService service;

    public WebMvcConfig(UserRoleService service) {
        this.service = service;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new Formatter<Set<Role>>() {
            @Override
            public Set<Role> parse(String text, Locale locale) {
                Set<Role> roleSet = new HashSet<>();
                String[] roles = text.split("^\\[|]$|(?<=]),\\s?");
                for (String roleString : roles) {
                    if (roleString.length() == 0) continue;
                    String authority =
                            roleString.substring(roleString.lastIndexOf("=") + 2,
                                    roleString.indexOf("]") - 1);
                    roleSet.add(service.getRoleByName(authority));
                }
                return roleSet;
            }

            @Override
            public String print(Set<Role> object, Locale locale) {
                return null;
            }
        });
    }
}
