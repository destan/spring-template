package dev.destan.template.user;

import com.fasterxml.jackson.annotation.JsonView;
import dev.destan.template.user.User.JsonViews.Public;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @JsonView(Public.class)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    @SequenceGenerator(name = "users_id_seq", sequenceName = "users_id_seq", allocationSize = 1)
    private Long id;

    @JsonView(Public.class)
    private String username;

    private String password;

    private boolean enabled;

    private boolean locked;

    private boolean expired;

    private LocalDateTime credentialsSetAt;

    @JsonView(Public.class)
    @ManyToMany(fetch = FetchType.EAGER) // We really always want to fetch roles with users
    private Set<Role> roles = new HashSet<>();

    User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.roles.add(role);
        this.credentialsSetAt = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // > GrantedAuthority instances are high-level permissions that the user is granted. Two examples are roles and scopes.
        // See https://docs.spring.io/spring-security/reference/servlet/authentication/architecture.html#servlet-authentication-granted-authority
        return roles; // TODO add permissions
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public static class JsonViews {
        public static class Public {
        }

        public static class Admin {
        }
    }
}
