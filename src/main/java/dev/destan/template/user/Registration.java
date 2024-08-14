package dev.destan.template.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
class Registration {

    private final List<String> REGISTRABLE_ROLES = List.of("ROLE_USER");// TODO from properties

    private final String DEFAULT_REGISTRATION_ROLE = "ROLE_USER";// TODO from properties

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;


    User register(RegisterRequest registerRequest) {

        final String roleName = StringUtils.defaultIfBlank(registerRequest.role(), DEFAULT_REGISTRATION_ROLE);

        if (!REGISTRABLE_ROLES.contains(roleName)) {
            throw new RuntimeException("Role '" + roleName + "' not allowed!");//TODO custom exception
        }

        final Role role = roleRepository.findByName(roleName).orElseThrow(() -> new IllegalStateException("Role '" + roleName + "' not found"));

        final User user = new User(registerRequest.username(), passwordEncoder.encode(registerRequest.password()), role);
        return userRepository.save(user);
    }

    List<Role> registrableRoles() {
        return roleRepository.registrableRoles(REGISTRABLE_ROLES);
    }
}
