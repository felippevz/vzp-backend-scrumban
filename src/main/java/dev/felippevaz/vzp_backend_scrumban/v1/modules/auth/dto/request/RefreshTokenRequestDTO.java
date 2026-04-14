package dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RefreshTokenRequestDTO {

    @NotBlank
    private String refreshToken;
}
