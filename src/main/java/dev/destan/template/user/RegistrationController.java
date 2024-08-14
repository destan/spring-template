package dev.destan.template.user;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("public/registration")
class RegistrationController {

    private final Registration registration;

    private final RoleRepository roleRepository;

    @GetMapping("roles")
    ResponseEntity<List<Role>> registrableRoles() {
        return ResponseEntity.ok(registration.registrableRoles());
    }

    @PostMapping
    @JsonView(User.JsonViews.Public.class)
    ResponseEntity<User> register(@RequestBody @Validated RegisterRequest request) {
        User user = registration.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
