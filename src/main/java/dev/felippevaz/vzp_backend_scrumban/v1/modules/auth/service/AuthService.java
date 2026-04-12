package dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.service;

import dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.dto.request.LoginRequestDTO;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.dto.request.RegisterRequestDTO;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.dto.response.LoginResponseDTO;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.dto.response.RegisterResponseDTO;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.mapper.AuthMapper;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.user.domain.User;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.user.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService  {

    private final UserRepository userRepository;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthService(UserRepository userRepository, AuthMapper authMapper, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userRepository = userRepository;
        this.authMapper = authMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        try {

            UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(request.username(), request.password());
            Authentication authentication = authenticationManager.authenticate(userAndPass);

            User user = (User) authentication.getPrincipal();
            String token = tokenService.generateToken(user);

            return new LoginResponseDTO(token);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
            //todo: treat error later
        }
    }

    public RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO) {
        return saveUser(registerRequestDTO);
    }

    //todo: treat error later
    private RegisterResponseDTO saveUser(RegisterRequestDTO registerRequestDTO) {
        if (userRepository.existsByUsername(registerRequestDTO.username()) || userRepository.existsByEmail(registerRequestDTO.email())) {
            throw new IllegalArgumentException("User already exists");
        }

        User user = new User();

        user.setPassword(passwordEncoder.encode(registerRequestDTO.password()));

        user.setUsername(registerRequestDTO.username());
        user.setEmail(registerRequestDTO.email());

        return authMapper.toRegisterResponseDTO(userRepository.save(user));
    }
}
