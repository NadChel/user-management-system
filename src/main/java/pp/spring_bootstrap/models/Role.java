package pp.spring_bootstrap.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;
import java.util.StringJoiner;

@Entity
@Table(name = "roles")
@Data
@EqualsAndHashCode // чтобы исключить userList c помощью @EqualsAndHashCode.Exclude
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;
    @Column(name = "role", nullable = false, unique = true)
    private String authority;
    @ManyToMany(mappedBy = "authorities")
    @EqualsAndHashCode.Exclude
    private Set<User> userList;

    public Role() {
    }

    public Role(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    @Override
    public String toString() { // StringBuilder внутри StringJoiner лучше ломбоковской конкатенации
        return new StringJoiner(", ", Role.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("role='" + authority + "'")
                .toString();
    }
}