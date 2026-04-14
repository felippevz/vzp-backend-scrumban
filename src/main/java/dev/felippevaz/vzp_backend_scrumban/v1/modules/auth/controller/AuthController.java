package dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.controller;

import dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.dto.request.LoginRequestDTO;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.dto.request.RefreshTokenRequestDTO;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.dto.request.RegisterRequestDTO;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.dto.response.LoginResponseDTO;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.dto.response.RegisterResponseDTO;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.service.AuthService;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.service.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthService authService, RefreshTokenService refreshTokenService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refreshToken(@Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        return ResponseEntity.ok(refreshTokenService.refreshToken(refreshTokenRequestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.authService.register(request));
    }
}
