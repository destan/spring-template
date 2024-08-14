package dev.destan.template.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Component
class JwtCreator {

    private final Algorithm algorithm;

    JwtCreator(@Value("${app.jwt.secret}") byte[] secret) {
        this.algorithm = Algorithm.HMAC256(secret);
    }

    JwtWithMetadata create(Authentication authentication) {
        final Instant now = Instant.now();
        final Instant expiresAt = now.plus(1, ChronoUnit.HOURS);  //FIXME parametric

        final String jwt = JWT.create()
                .withJWTId(UUID.randomUUID().toString())
                .withIssuer("api") //FIXME parametric
                .withAudience("spring-template") //FIXME parametric and then use tenantId
                .withSubject(authentication.getName())
                .withExpiresAt(expiresAt)
                .withIssuedAt(now)
                .sign(algorithm);

        return new JwtWithMetadata(jwt, expiresAt.toEpochMilli());
    }

    record JwtWithMetadata(String jwt, long expiresAt) {}

}
