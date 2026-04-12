package dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "api.v1.token")
public class TokenProperties {

    private String secret;
    private String issuer;
    private Long expirationSeconds;
}
