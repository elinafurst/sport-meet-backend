package se.elfu.sportprojectbackend.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.elfu.sportprojectbackend.repository.model.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUserNumber(UUID userNumber);
    Optional<User> findByAccountEmail(String currentUserName);
    Optional<User> findByUsernameIgnoreCase(String alias);

}
