package dev.destan.template.security;

import java.util.List;

/**
 * Represents a user extracted from JWT.
 * <p>
 * Created on May, 2022
 *
 * @author destan
 */
public record UserFromJwt(long id, String username, List<String> roles) {

}
