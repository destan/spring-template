package dev.destan.template.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
class JwtRevocationInMemoryImpl implements JwtRevocationApi {

    private final Map<String, Long> revokedJwts = new ConcurrentHashMap<>();

    @Override
    public boolean revokeJwt(@NonNull String id, long expiresAt) {
        revokedJwts.put(id, expiresAt);
        return true;
    }

    @Override
    public boolean revokeJwts(@NonNull Set<JwtToRevoke> jwtsToRevoke) {
        jwtsToRevoke.forEach(jwtToRevoke -> revokedJwts.put(jwtToRevoke.id(), jwtToRevoke.expiresAt()));
        return true;
    }

    @Override
    public boolean isJwtRevoked(@NonNull String id) {
        return revokedJwts.containsKey(id);
    }
}
