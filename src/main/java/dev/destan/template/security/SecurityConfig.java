package dev.destan.template.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // Thanks: https://stackoverflow.com/a/71449312/878361
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtConfigurer jwtConfigurer) throws Exception {
        //@formatter:off

		// BEWARE: https://github.com/spring-projects/spring-security/issues/13568
		http
			.headers(hc -> hc.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
			.authorizeHttpRequests(
				(authorize) -> authorize
					.requestMatchers(HttpMethod.POST, "/api/login").permitAll()
					.requestMatchers(HttpMethod.GET, "/", "/error", "/index.html", "/demo").permitAll()
					.requestMatchers("/public", "/public/").denyAll()
					.requestMatchers("/public/**").permitAll()
					.requestMatchers(PathRequest.toH2Console()).permitAll()

					.anyRequest().authenticated())
			.with(jwtConfigurer, Customizer.withDefaults())
			.csrf(AbstractHttpConfigurer::disable) // If we disable csrf in jwtConfigurer it won't work for some reason
			.sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // If we set stateless in jwtConfigurer it won't work for some reason
			;
		//@formatter:on

        return http.build();
    }

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}