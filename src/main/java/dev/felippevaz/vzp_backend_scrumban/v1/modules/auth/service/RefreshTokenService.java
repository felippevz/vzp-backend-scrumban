package dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.service;

import dev.felippevaz.vzp_backend_scrumban.v1.commons.domain.ErrorData;
import dev.felippevaz.vzp_backend_scrumban.v1.commons.domain.SecurityLoggerEvent;
import dev.felippevaz.vzp_backend_scrumban.v1.commons.exceptions.RequestException;
import dev.felippevaz.vzp_backend_scrumban.v1.commons.handler.SecurityAuditHandler;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.domain.RefreshToken;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.dto.request.RefreshTokenRequestDTO;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.dto.response.LoginResponseDTO;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.properties.RefreshTokenProperties;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.repository.RefreshTokenRepository;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.user.domain.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenProperties refreshTokenProperties;
    private final SecurityAuditHandler securityAuditHandler;
    private final TokenService tokenService;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, RefreshTokenProperties refreshTokenProperties, SecurityAuditHandler securityAuditHandler, TokenService tokenService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenProperties = refreshTokenProperties;
        this.securityAuditHandler = securityAuditHandler;
        this.tokenService = tokenService;
    }

    public RefreshToken create(User user) {

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plus(refreshTokenProperties.getExpiresIn(), ChronoUnit.DAYS));

        securityAuditHandler.log(SecurityLoggerEvent.REFRESH_TOKEN_CREATED, user.getUsername());

        return refreshTokenRepository.save(refreshToken);
    }

    public LoginResponseDTO refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO) {
        return this.refreshTokenRepository.findByToken(refreshTokenRequestDTO.getRefreshToken())
                .map(this::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = tokenService.generateToken(user);
                    return new LoginResponseDTO(accessToken, refreshTokenRequestDTO.getRefreshToken());
                })
                .orElseThrow(() -> new RequestException(ErrorData.INVALID_REFRESH_TOKEN));
    }

    private boolean exists(String token) {
        return refreshTokenRepository.findByToken(token).isPresent();
    }

    private RefreshToken verifyExpiration(RefreshToken token) {

        if(token.getExpiryDate().isBefore(Instant.now())) {

            securityAuditHandler.log(SecurityLoggerEvent.REFRESH_TOKEN_EXPIRED, token.getUser().getUsername());

            refreshTokenRepository.delete(token);

            throw new RequestException(ErrorData.INVALID_REFRESH_TOKEN);
        }

        return token;
    }
}
