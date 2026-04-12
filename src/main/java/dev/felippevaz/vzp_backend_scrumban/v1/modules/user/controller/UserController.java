package dev.felippevaz.vzp_backend_scrumban.v1.modules.user.controller;

import dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.domain.JWTUserDATA;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.user.domain.UserRole;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.user.dto.request.UserRequestDTO;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.user.dto.response.UserResponseDTO;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(this.service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> read(@PathVariable Long id, @AuthenticationPrincipal JWTUserDATA jwtUserDATA) {
        validateAuthority(id, jwtUserDATA);
        return ResponseEntity.ok(this.service.read(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @RequestBody @Valid UserRequestDTO userRequestDTO, @AuthenticationPrincipal JWTUserDATA jwtUserDATA) {
        validateAuthority(id, jwtUserDATA);
        return ResponseEntity.ok(this.service.update(id, userRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,  @AuthenticationPrincipal JWTUserDATA jwtUserDATA) {
        validateAuthority(id, jwtUserDATA);
        this.service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private void validateAuthority(Long targetId, JWTUserDATA jwtUserDATA) {
        if (!Objects.equals(jwtUserDATA.role(), UserRole.ADMIN) && !Objects.equals(jwtUserDATA.userId(), targetId))
            throw new AccessDeniedException("Access denied: insufficient permissions");
    }
}