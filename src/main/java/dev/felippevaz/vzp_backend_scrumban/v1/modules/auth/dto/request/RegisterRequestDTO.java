package dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record RegisterRequestDTO(

        @NotBlank(message = "Username is mandatory")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        String username,

        @NotBlank(message = "Password is mandatory")
        @Size(min = 8, message = "Password must be least 8 characters long")
        String password,

        @NotBlank(message = "Email is mandatory")
        @Email(message = "Invalid email")
        String email,

        @URL(message = "Invalid URL")
        String urlImageUser
) {
}
