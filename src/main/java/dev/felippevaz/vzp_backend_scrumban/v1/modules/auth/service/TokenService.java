package dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.domain.JWTUserDATA;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.properties.TokenProperties;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.user.domain.User;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.user.domain.UserRole;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class TokenService {

    private final TokenProperties tokenProperties;

    public TokenService(TokenProperties tokenProperties) {
        this.tokenProperties = tokenProperties;
    }

    public String generateToken(User user) {

        Algorithm algorithm = Algorithm.HMAC256(tokenProperties.getSecret());

        return JWT.create()
                .withIssuer(tokenProperties.getIssuer())
                .withClaim("userId", user.getId())
                .withClaim("globalRole", user.getRole().name())
                .withSubject(user.getUsername())
                .withExpiresAt(Instant.now().plusSeconds(tokenProperties.getExpirationSeconds()))
                .withIssuedAt(Instant.now())
                .sign(algorithm);
    }

    public Optional<JWTUserDATA> validateToken(String token) {
        try {

            Algorithm algorithm = Algorithm.HMAC256(tokenProperties.getSecret());

            DecodedJWT decode = JWT.require(algorithm)
                    .withIssuer(tokenProperties.getIssuer())
                    .acceptLeeway(5)
                    .build()
                    .verify(token);

            return Optional.of(JWTUserDATA.builder()
                    .userId(decode.getClaim("userId").asLong())
                    .role(UserRole.fromString(decode.getClaim("globalRole").asString()).orElse(UserRole.USER))
                    .username(decode.getSubject())
                    .build());

        } catch (JWTVerificationException exception) {
            return Optional.empty();
        }
    }
}
