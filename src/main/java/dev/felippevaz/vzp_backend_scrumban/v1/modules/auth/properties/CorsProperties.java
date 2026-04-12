package dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "api.v1.cors")
public class CorsProperties {

    String url;
    List<String> methods;
    String headers;
}
