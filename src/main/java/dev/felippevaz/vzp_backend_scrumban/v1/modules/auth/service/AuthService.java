package dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.service;

import dev.felippevaz.vzp_backend_scrumban.v1.commons.domain.ErrorData;
import dev.felippevaz.vzp_backend_scrumban.v1.commons.domain.SecurityLoggerEvent;
import dev.felippevaz.vzp_backend_scrumban.v1.commons.exceptions.RequestException;
import dev.felippevaz.vzp_backend_scrumban.v1.commons.handler.SecurityAuditHandler;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.domain.RefreshToken;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.dto.request.LoginRequestDTO;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.dto.request.RefreshTokenRequestDTO;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.dto.request.RegisterRequestDTO;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.dto.response.LoginResponseDTO;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.dto.response.RegisterResponseDTO;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.mapper.AuthMapper;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.user.domain.User;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.user.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService  {

    private final UserService userService;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final SecurityAuditHandler  securityAuditHandler;
    private final RefreshTokenService refreshTokenService;

    public AuthService(UserService userService, AuthMapper authMapper, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenService tokenService, SecurityAuditHandler securityAuditHandler, RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.authMapper = authMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.securityAuditHandler = securityAuditHandler;
        this.refreshTokenService = refreshTokenService;
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        try {

            userService.getUser(request.username());

            UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(request.username(), request.password());
            Authentication authentication = authenticationManager.authenticate(userAndPass);

            User authenticadUser = (User) authentication.getPrincipal();
            assert authenticadUser != null;

            String token = tokenService.generateToken(authenticadUser);

            RefreshToken refreshToken = refreshTokenService.create(authenticadUser);

            securityAuditHandler.log(SecurityLoggerEvent.LOGIN_SUCCESS, authenticadUser.getUsername());

            return new LoginResponseDTO(token, refreshToken.getToken());
        } catch (BadCredentialsException e) {

            securityAuditHandler.log(SecurityLoggerEvent.LOGIN_FAILED_CREDENTIALS, request.username());

            throw new RequestException(ErrorData.BAD_CREDENTIALS);
        }
    }

    public RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO) {
        return saveUser(registerRequestDTO);
    }

    public LoginResponseDTO refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO) {
        return refreshTokenService.refreshToken(refreshTokenRequestDTO);
    }

    private RegisterResponseDTO saveUser(RegisterRequestDTO registerRequestDTO) {

        if (userService.exists(registerRequestDTO.username(), registerRequestDTO.email())) {
            securityAuditHandler.log(SecurityLoggerEvent.REGISTER_FAILED, registerRequestDTO.username());
            throw new RequestException(ErrorData.BAD_CREDENTIALS);
        }

        User user = new User();

        user.setPassword(passwordEncoder.encode(registerRequestDTO.password()));

        user.setUsername(registerRequestDTO.username());
        user.setEmail(registerRequestDTO.email());

        securityAuditHandler.log(SecurityLoggerEvent.REGISTER_SUCCESS, user.getUsername());

        return authMapper.toRegisterResponseDTO(userService.create(user));
    }
}
