package dev.destan.template.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.stereotype.Component;

/**
 * Created on May, 2022
 *
 * @author destan
 */
@Component
class JwtConfigurer extends AbstractHttpConfigurer<JwtConfigurer, HttpSecurity> {

	private final JwtAuthenticationProvider jwtAuthenticationProvider;

	JwtConfigurer(@Value("${app.jwt.secret}") byte[] secret) {
		this.jwtAuthenticationProvider = new JwtAuthenticationProvider(secret);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		final AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);

		http
			.addFilterBefore(new JwtAuthenticationFilter(authenticationManager), AuthorizationFilter.class)
			.authenticationProvider(jwtAuthenticationProvider)
			.exceptionHandling(exceptionHandling -> exceptionHandling
					.authenticationEntryPoint(new JwtAuthenticationEntryPoint())
					.accessDeniedHandler(new JwtAccessDeniedHandler())
			)
			//.csrf(AbstractHttpConfigurer::disable) // If we disable csrf here, it won't work for some reason
			//.sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // If we set here, it won't work for some reason
		;
	}
}
