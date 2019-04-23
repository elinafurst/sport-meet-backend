package se.elfu.sportprojectbackend.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import se.elfu.sportprojectbackend.repository.model.Unit;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UnitRepository extends PagingAndSortingRepository<Unit, Long> {
    Optional<Unit> findByUnitNumber(UUID groupNumber);
    Optional<Unit> findByNameIgnoreCase(String name);
}
