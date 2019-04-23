package se.elfu.sportprojectbackend.repository;

import org.springframework.data.repository.CrudRepository;
import se.elfu.sportprojectbackend.repository.model.Account;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long> {
    Optional<Account> findByEmail(String username);
}
