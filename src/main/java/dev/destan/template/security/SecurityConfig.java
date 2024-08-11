package dev.destan.template.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	// Thanks: https://stackoverflow.com/a/71449312/878361
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, LoginAuthenticationSuccessHandler successHandler, JwtConfigurer jwtConfigurer) throws Exception {
		//@formatter:off
		http.formLogin((form) -> form
				.loginPage("/login") // if we remove this line then default login page is used
				.permitAll()
				.successHandler(successHandler)
		);

		// http.anonymous(AbstractHttpConfigurer::disable);

		// BEWARE: https://github.com/spring-projects/spring-security/issues/13568
		http
				.with(jwtConfigurer, Customizer.withDefaults())
				.authorizeHttpRequests((authorize) -> authorize
				.requestMatchers("", "/", "/error", "/index.html", "/login.html", "/demo", "/login", "/api/login").permitAll()
				.anyRequest().authenticated())
				.csrf(AbstractHttpConfigurer::disable) // If we disable csrf in jwtConfigurer it won't work for some reason
			;
		//@formatter:on

		return http.build();
	}

	@Bean
	public UserDetailsService userDetailsService() {
	    UserDetails user =
	            User.withDefaultPasswordEncoder()
	                    .username("joe")
	                    .password("123qwe123")
	                    // .roles("USER")
	                    .authorities("USER")
	                    .build();

	    UserDetails admin =
	            User.withDefaultPasswordEncoder()
	                    .username("jane")
	                    .password("123qwe123")
	                    .roles("ADMIN")
	                    .build();

	    return new InMemoryUserDetailsManager(user, admin);
	}
}

