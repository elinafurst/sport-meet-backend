package se.elfu.sportprojectbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.elfu.sportprojectbackend.repository.model.Location;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findByCity(String city);
}
