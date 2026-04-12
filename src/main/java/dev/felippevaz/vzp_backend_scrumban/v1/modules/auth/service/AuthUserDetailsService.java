package dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.service;

import dev.felippevaz.vzp_backend_scrumban.v1.modules.user.service.UserService;
import lombok.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public AuthUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) {
        return this.getUserDetailsByUser(username);
    }

    private UserDetails getUserDetailsByUser(String username) {
        return userService.getUser(username);
    }
}
