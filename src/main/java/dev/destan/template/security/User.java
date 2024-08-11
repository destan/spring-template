package dev.destan.template.security;

import java.util.List;

/**
 * Represents a user extracted from JWT.
 * <p>
 * Created on May, 2022
 *
 * @author destan
 */
public record User(String email, String fullName, List<String> roles) {

}
