package dev.destan.template.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * This class is responsible for creating the JWT when form login is used.
 */
@Component
@RequiredArgsConstructor
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtCreator jwtCreator;

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        final JwtCreator.JwtWithMetadata jwtResponse = jwtCreator.create(authentication);
        final String responseBody = objectMapper.writeValueAsString(jwtResponse);

        response.setHeader("Content-Type", "application/json; charset=UTF-8");
        response.getWriter().write(responseBody);
        response.getWriter().flush();
    }
}
