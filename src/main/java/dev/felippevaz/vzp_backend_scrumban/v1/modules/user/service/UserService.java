package dev.felippevaz.vzp_backend_scrumban.v1.modules.user.service;

import dev.felippevaz.vzp_backend_scrumban.v1.commons.domain.ErrorData;
import dev.felippevaz.vzp_backend_scrumban.v1.commons.domain.SecurityLoggerEvent;
import dev.felippevaz.vzp_backend_scrumban.v1.commons.exceptions.RequestException;
import dev.felippevaz.vzp_backend_scrumban.v1.commons.handler.SecurityAuditHandler;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.user.domain.User;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.user.dto.request.UserUpdateDTO;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.user.dto.response.UserResponseDTO;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.user.mapper.UserMapper;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SecurityAuditHandler securityAuditHandler;

    public UserService(UserRepository userRepository, UserMapper userMapper, SecurityAuditHandler securityAuditHandler) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.securityAuditHandler = securityAuditHandler;
    }

    public Page<UserResponseDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toResponseDTO);
    }

    public UserResponseDTO read(Long id) {
        return userMapper.toResponseDTO(getUser(id));
    }

    @Transactional
    public UserResponseDTO update(Long id, UserUpdateDTO userUpdateDTO) {

        User user = getUser(id);

        if(!userUpdateDTO.email().equals(user.getEmail()) && userRepository.existsByEmail(userUpdateDTO.email())) {
            throw new RequestException(ErrorData.INVALID_UPDATE_EMAIL);
        }

        this.userMapper.updateFromDTO(userUpdateDTO, user);

        return userMapper.toResponseDTO(user);
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    private User getUser(Long id) {
        return userRepository.findById(id)
                    .orElseThrow(() -> new RequestException(ErrorData.USER_NOT_FOUND)
                );
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    securityAuditHandler.log(SecurityLoggerEvent.LOGIN_FAILED_USER_NOT_FOUND, username);
                    return new RequestException(ErrorData.BAD_CREDENTIALS);
                });
    }

    public boolean exists(String username, String email) {
        return userRepository.existsByUsername(username) || userRepository.existsByEmail(email);
    }

    public User create(User user) {
        return userRepository.save(user);
    }
}
