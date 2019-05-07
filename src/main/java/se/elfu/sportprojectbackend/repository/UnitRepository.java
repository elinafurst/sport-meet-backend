package se.elfu.sportprojectbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import se.elfu.sportprojectbackend.repository.model.Unit;
import se.elfu.sportprojectbackend.repository.model.User;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UnitRepository extends PagingAndSortingRepository<Unit, Long> {
    Optional<Unit> findByUnitNumber(UUID groupNumber);
    Optional<Unit> findByNameIgnoreCase(String name);

    Page<Unit> findByAdminsIn(User user, Pageable unitPageRequest);

    Set<Unit> findByAdminsIn(User user);

}
