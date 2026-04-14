package dev.felippevaz.vzp_backend_scrumban.v1.commons.handler;

import dev.felippevaz.vzp_backend_scrumban.v1.commons.domain.SecurityLoggerEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SecurityAuditHandler {

    public void log(SecurityLoggerEvent event, Object... args) {
        String formattedMessage = String.format("[%s] %s", event.getCode(), event.getMessageTemplate());

        switch (event.getLevel()) {
            case INFO -> log.info(formattedMessage, args);
            case WARN -> log.warn(formattedMessage, args);
            case ERROR -> log.error(formattedMessage, args);
            case DEBUG -> log.debug(formattedMessage, args);
            default -> log.trace(formattedMessage, args);
        }
    }
}
