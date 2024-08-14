package dev.destan.template.security;

import dev.destan.template.config.ApplicationProperties;
import dev.destan.template.security.JwtRevocationApi.JwtToRevoke;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/logout")
class LogoutController {

    private final JwtRevocationApi jwtRevocationApi;

    private final ApplicationProperties applicationProperties;

    @PostMapping
    ResponseEntity<Void> logout(Principal principal) {

        if (principal instanceof JwtAuthentication jwtAuthentication) {

            // TODO move below logic into a LogoutApi
            if (applicationProperties.getLogout().invalidateAll()) {
                Set<JwtToRevoke> jwtsToRevoke = Set.of(new JwtToRevoke(jwtAuthentication.getJwtId(), jwtAuthentication.expiresAt())); // FIXME get from db
                jwtRevocationApi.revokeJwts(jwtsToRevoke);
            }
            else {
                jwtRevocationApi.revokeJwt(jwtAuthentication.getJwtId(), jwtAuthentication.expiresAt());
            }

            return ResponseEntity.noContent().build();
        }
        throw new IllegalStateException("Principle is of type " + principal.getClass().getCanonicalName());
    }

}
