package se.elfu.sportprojectbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.elfu.sportprojectbackend.repository.model.Sport;

import java.util.Optional;

public interface SportRepository extends JpaRepository<Sport, Long> {
    Optional<Sport> findByName(String sport);

    Optional<Sport> findByNameIgnoreCase(String sport);
}
