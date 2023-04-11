package pp.spring_bootstrap.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pp.spring_bootstrap.models.Role;

import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Function;

@Configuration
public class WebMvcConfig {
    @Bean
    public Function<Set<Role>, String> jsonify() {
        return s -> {
            StringJoiner sj = new StringJoiner(",\n", "[\n", "\n]");
            for (Role role : s) {
                sj.add(String.format("{\n\"id\" : %d,\n\"authority\" : \"%s\"\n}", role.getId(), role.getAuthority()));
            }
            return sj.toString();
        };
    }
}
