package dev.destan.template.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.RegisteredClaims;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.util.Assert;

@Slf4j
public class JwtAuthenticationProvider implements AuthenticationProvider {

	private final JWTVerifier jwtVerifier;

	private final MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

	public JwtAuthenticationProvider(byte[] secret) {
		//@formatter:off
		this.jwtVerifier = JWT.require(Algorithm.HMAC256(secret))
							  .withClaimPresence(RegisteredClaims.JWT_ID)
							  .withClaimPresence(RegisteredClaims.AUDIENCE)
							  .withClaimPresence(RegisteredClaims.SUBJECT)
							  .withClaimPresence(RegisteredClaims.ISSUER)
							  .build();
		//@formatter:on
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		// This method is called from JwtAuthenticationFilter#attemptAuthentication with an unauthenticated JwtAuthentication object

		Assert.isInstanceOf(JwtAuthentication.class, authentication,
				() -> this.messages.getMessage("JwtAuthenticationProvider.onlySupports",
						"Only JwtAuthentication is supported"));

		final JwtAuthentication jwt = (JwtAuthentication) authentication;
		try {
			jwt.verify(jwtVerifier);
			if (log.isDebugEnabled()) {
				log.debug("Authenticated with jwt with scopes {}", jwt.getAuthorities());
			}
			return jwt;
		}
		catch (JWTVerificationException e) {
			log.error(e.getMessage());//FIXME double log?
			throw new BadCredentialsException("Not a valid token", e);
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return JwtAuthentication.class.isAssignableFrom(authentication);
	}

}
