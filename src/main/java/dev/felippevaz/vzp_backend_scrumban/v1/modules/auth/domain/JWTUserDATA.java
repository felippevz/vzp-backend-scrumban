package dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.domain;

import dev.felippevaz.vzp_backend_scrumban.v1.modules.user.domain.UserRole;
import lombok.Builder;

@Builder
public record JWTUserDATA(

        Long userId,
        String username,
        UserRole role

) { }
