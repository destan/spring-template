package dev.destan.template.security;

import dev.destan.template.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

interface LoginUserRepository extends JpaRepository<User, Long> {

    // Don't use left join because UserDetails contract states that:
    // > Throws: UsernameNotFoundException – if the user could not be found or the user has no GrantedAuthority
    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.username = :username")
    Optional<User> findByUsername(String username);
}
