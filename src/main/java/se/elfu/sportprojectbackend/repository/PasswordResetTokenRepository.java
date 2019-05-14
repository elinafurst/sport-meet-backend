package se.elfu.sportprojectbackend.repository;

import ch.qos.logback.core.pattern.parser.OptionTokenizer;
import org.springframework.data.jpa.repository.JpaRepository;
import se.elfu.sportprojectbackend.repository.model.PasswordResetToken;

import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(UUID token);
}
