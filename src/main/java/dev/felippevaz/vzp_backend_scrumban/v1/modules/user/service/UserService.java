package dev.felippevaz.vzp_backend_scrumban.v1.modules.user.service;

import dev.felippevaz.vzp_backend_scrumban.v1.modules.user.domain.User;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.user.dto.request.UserRequestDTO;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.user.dto.response.UserResponseDTO;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.user.mapper.UserMapper;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public Page<UserResponseDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toResponseDTO);
    }

    public UserResponseDTO read(Long id) {
        return userMapper.toResponseDTO(getUser(id));
    }

    @Transactional
    public UserResponseDTO update(Long id, UserRequestDTO userRequestDTO) {

        User user = getUser(id);
        this.userMapper.updateEntityFromDTO(userRequestDTO, user);

        return userMapper.toResponseDTO(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    private User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        //todo: treat error later
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username not found"));
        //todo: treat error later / default error message
    }
}
