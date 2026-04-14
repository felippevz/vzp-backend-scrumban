package dev.felippevaz.vzp_backend_scrumban.v1.commons.domain;

import lombok.Getter;
import org.slf4j.event.Level;

@Getter
public enum SecurityLoggerEvent {

    LOGIN_SUCCESS(Level.INFO, "AUTH-001", "Successful login for user: {}"),
    LOGIN_FAILED_CREDENTIALS(Level.WARN, "AUTH-002", "Login failed (incorrect password) requested user: {}"),
    LOGIN_FAILED_USER_NOT_FOUND(Level.WARN, "AUTH-003", "Login failed (nonexistent user) requested user: {}"),
    REGISTER_FAILED(Level.WARN, "AUTH-004", "Register failed for user (credentials exists) requested user: {}"),
    REGISTER_SUCCESS(Level.INFO, "AUTH-005", "Register success for user: {}"),
    TOKEN_EXPIRED(Level.WARN, "AUTH-006", "Expired token detected. IP: {}"),
    ACCESS_DENIED(Level.ERROR, "AUTH-007", "Access denied to protected route: {} | IP: {}"),
    REFRESH_TOKEN_CREATED(Level.INFO, "AUTH-008", "Refresh token created for user: {}"),
    REFRESH_TOKEN_EXPIRED(Level.INFO, "AUTH-009", "Refresh token expired for user: {}"),;

    private final Level level;
    private final String code;
    private final String messageTemplate;


    SecurityLoggerEvent(Level level, String code, String messageTemplate) {
        this.level = level;
        this.code = code;
        this.messageTemplate = messageTemplate;
    }
}
