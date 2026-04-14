package dev.felippevaz.vzp_backend_scrumban.v1.modules.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record UserUpdateDTO(

        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        String username,

        @Email(message = "Invalid email")
        String email,

        @URL(message = "Invalid URL")
        String urlImageUser

) {}
