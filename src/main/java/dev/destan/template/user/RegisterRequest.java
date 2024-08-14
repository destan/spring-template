package dev.destan.template.user;

import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.Nullable;

record RegisterRequest(@NotBlank String username, @NotBlank String password, @Nullable String role) {
    @Override
    public @NotBlank String password() {
        return password.strip();
    }

    @Override
    public @NotBlank String username() {
        return username.strip();
    }

    @Override
    public @Nullable String role() {
        return role == null ? null : role.strip();
    }
}
