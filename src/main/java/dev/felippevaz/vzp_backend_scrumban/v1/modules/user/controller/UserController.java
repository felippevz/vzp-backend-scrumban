package dev.felippevaz.vzp_backend_scrumban.v1.modules.user.controller;

import dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.domain.JWTUserDATA;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.user.dto.request.UserUpdateDTO;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.user.dto.response.UserResponseDTO;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponseDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(this.service.findAll(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(#id, principal)")
    public ResponseEntity<UserResponseDTO> read(@PathVariable Long id) {
        return ResponseEntity.ok(this.service.read(id));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(#id, principal)")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @RequestBody @Valid UserUpdateDTO userUpdateDTO) {
        return ResponseEntity.ok(this.service.update(id, userUpdateDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(#id, principal)")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        this.service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/projects")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> readProjects(@PathVariable Long id) {
        return null;
        //todo: create module PROJECTS for return pages
    }

    @GetMapping("/me/projects")
    public ResponseEntity<Page<?>> findProjectsByJWT(@AuthenticationPrincipal JWTUserDATA jwtUserDATA) {
        return null;
        //todo: create module PROJECTS for return pages
    }

    @GetMapping("/me/tasks")
    public ResponseEntity<Page<?>> findTasksByJWT(@AuthenticationPrincipal JWTUserDATA jwtUserDATA) {
        return null;
        //todo: create module TASKS for return pages
    }
}