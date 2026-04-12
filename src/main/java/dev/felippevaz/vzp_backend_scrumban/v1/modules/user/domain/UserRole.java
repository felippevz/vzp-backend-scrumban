package dev.felippevaz.vzp_backend_scrumban.v1.modules.user.domain;

import lombok.Getter;

import java.util.Optional;

@Getter
public enum UserRole {

    USER("USER"),
    ADMIN("ADMIN");

    private final String name;

    UserRole(String name) {
        this.name = name;
    }

    public static Optional<UserRole> fromString(String name) {
        for (UserRole role : UserRole.values()) {
            if (role.name().equalsIgnoreCase(name))
                return Optional.of(role);
        }
        return Optional.empty();
    }
}
