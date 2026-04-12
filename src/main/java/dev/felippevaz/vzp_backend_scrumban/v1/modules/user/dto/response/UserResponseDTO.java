package dev.felippevaz.vzp_backend_scrumban.v1.modules.user.dto.response;

import dev.felippevaz.vzp_backend_scrumban.v1.modules.user.domain.UserStatus;

import java.time.LocalDateTime;

public record UserResponseDTO(
        Long id,
        String username,
        String email,
        String urlImageUser,
        UserStatus status,
        LocalDateTime createdAt
) {
}
