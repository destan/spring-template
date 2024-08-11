package dev.destan.template.security;

import dev.destan.template.security.JwtCreator.JwtWithMetadata;
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
@RequestMapping("api")
class LoginController {

    private final AuthenticationManager authenticationManager;

    private final JwtCreator jwtCreator;

    LoginController(AuthenticationManager authenticationManager, JwtCreator jwtCreator) {
        this.authenticationManager = authenticationManager;
        this.jwtCreator = jwtCreator;
    }

    @PostMapping("login")
    ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));

            final JwtWithMetadata jwtResponse = jwtCreator.create(authentication);

            return ResponseEntity.ok(new LoginResponse(jwtResponse));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    record LoginRequest(String username, String password) { }

    record LoginResponse(String token, long expiresAt) {
        public LoginResponse(JwtWithMetadata jwtWithMetadata) {
            this(jwtWithMetadata.jwt(), jwtWithMetadata.expiresAt());
        }
    }

}
