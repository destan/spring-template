package dev.destan.template.security;

import org.springframework.lang.NonNull;

import java.util.Set;

public interface JwtRevocationApi {

    /**
     * @param id       JWT id that is denoted by jti claim.
     * @param expiresAt epoch milliseconds at which the JWT will expire
     * @return true if the JWT with the given id is successfully revoked, false otherwise
     * @see <a href=https://www.iana.org/assignments/jwt/jwt.xhtml#claims>https://www.iana.org/assignments/jwt/jwt.xhtml#claims</a>
     */
    boolean revokeJwt(@NonNull String id, long expiresAt);

    boolean revokeJwts(@NonNull Set<JwtToRevoke> jwtsToRevoke);

    boolean isJwtRevoked(@NonNull String id);

    record JwtToRevoke(String id, long expiresAt) {}

}
