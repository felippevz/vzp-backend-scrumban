package dev.felippevaz.vzp_backend_scrumban.v1.commons.service;

import dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.domain.JWTUserDATA;
import org.springframework.stereotype.Component;

@Component("securityService")
public class SecurityService {

    public boolean isOwner(Long targetId, JWTUserDATA principal) {
        return principal.userId().equals(targetId);
    }
}
