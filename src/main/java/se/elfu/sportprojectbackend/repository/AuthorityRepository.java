package se.elfu.sportprojectbackend.repository;

import org.springframework.data.repository.CrudRepository;
import se.elfu.sportprojectbackend.repository.model.Authority;

public interface AuthorityRepository extends CrudRepository<Authority, Long> {

    Authority findByAuthority(String role_user);
}
