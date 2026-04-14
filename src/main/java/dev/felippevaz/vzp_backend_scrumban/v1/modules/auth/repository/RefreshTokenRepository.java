package dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.repository;

import dev.felippevaz.vzp_backend_scrumban.v1.modules.auth.domain.RefreshToken;
import dev.felippevaz.vzp_backend_scrumban.v1.modules.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);
}
