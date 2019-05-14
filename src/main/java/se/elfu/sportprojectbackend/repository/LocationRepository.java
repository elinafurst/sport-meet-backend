package se.elfu.sportprojectbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.elfu.sportprojectbackend.repository.model.Location;

import java.util.Optional;
import java.util.Set;

public interface LocationRepository extends JpaRepository<Location, Long> {

    Set<Location> findByCity(String city);

    Optional<Location> findByCityAndAreaArea(String city, String area);

    int countByCity(String city);
}
