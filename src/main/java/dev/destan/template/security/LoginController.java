package dev.destan.template.security;

import dev.destan.template.security.JwtCreator.JwtWithMetadata;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/login")
class LoginController {

    private final AuthenticationManager authenticationManager;

    private final JwtCreator jwtCreator;

    @PostMapping
    ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));

            final JwtWithMetadata jwtResponse = jwtCreator.create(authentication);

            return ResponseEntity.ok(new LoginResponse(jwtResponse));
        } catch (BadCredentialsException ex) { // FIXME let error handlers handle this
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("refresh")
    ResponseEntity<LoginResponse> refresh(@RequestBody LoginRequest loginRequest) {

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));

            final JwtWithMetadata jwtResponse = jwtCreator.create(authentication);

            return ResponseEntity.ok(new LoginResponse(jwtResponse));
        } catch (BadCredentialsException ex) { // FIXME let error handlers handle this
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    record LoginRequest(@NotBlank String username, @NotBlank String password) {
        @Override
        public @NotBlank String password() {
            return password.strip();
        }

        @Override
        public @NotBlank String username() {
            return username.strip();
        }
    }

    record LoginResponse(@NotBlank String token, long expiresAt) { // TODO refresh token, user
        public LoginResponse(JwtWithMetadata jwtWithMetadata) {
            this(jwtWithMetadata.jwt(), jwtWithMetadata.expiresAt());
        }
    }

}
