package se.elfu.sportprojectbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;
import se.elfu.sportprojectbackend.repository.model.Event;
import se.elfu.sportprojectbackend.repository.model.Unit;
import se.elfu.sportprojectbackend.repository.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface EventRepository extends PagingAndSortingRepository<Event, Long>, QueryByExampleExecutor<Event> {

    Page<Event> findByEventStartBetweenAndActiveTrue(LocalDateTime parseDate, LocalDateTime parseDate1, Pageable pageRequest);

    Page<Event> findByEventStartBetweenAndSportNameAndLocationCityAndActiveTrue(LocalDateTime parseDateTime, LocalDateTime parseDateTime1, String type, String city, Pageable pageRequest);

    Page<Event> findByEventStartBetweenAndSportNameAndActiveTrue(LocalDateTime parseDateTime, LocalDateTime parseDateTime1, String type, Pageable pageRequest);

    Page<Event> findByEventStartGreaterThanEqualAndSportNameAndLocationCityAndLocationAreaAreaAndActiveTrue(LocalDateTime parseDateTime, String type, String city, String area, Pageable pageRequest);

    Page<Event> findByEventStartGreaterThanEqualAndSportNameAndLocationCityAndActiveTrue(LocalDateTime parseDateTime, String type, String city, Pageable pageRequest);

    Page<Event> findByEventStartGreaterThanEqualAndSportNameAndActiveTrue(LocalDateTime parseDateTime, String type, Pageable pageRequest);

    Page<Event> findByEventStartGreaterThanEqualAndActiveTrue(LocalDateTime parseDateTime, String type, Pageable pageRequest);

    Page<Event> findByEventStartBetweenAndSportNameAndLocationCityAndLocationAreaAreaAndActiveTrue(LocalDateTime now, LocalDateTime parseDateTime, String type, String city, String area, Pageable pageRequest);

    Page<Event> findByEventStartBetweenAndLocationCityAndActiveTrue(LocalDateTime now, LocalDateTime parseDateTime, String city, Pageable pageRequest);

    Page<Event> findByEventStartGreaterThanEqualAndLocationCityAndActiveTrue(LocalDateTime parseDateTime, String city, Pageable pageRequest);

    Optional<Event> findByEventNumber(UUID eventNumber);

    Page<Event> findByByUnit(Unit unitNumber, Pageable pageable);

    Page<Event> findByByUnitIn(Set<Unit> units, Pageable pageable);

    Long countByByUnit(Unit unitNumber);

    List<Event> findByEventStartLessThanAndActiveTrue(LocalDateTime expirationDateTime);

    Page<Event> findByCreatedByAndActiveTrue(User user, Pageable eventPageRequest);

    Page<Event> findByCreatedByAndActiveFalse(User user, Pageable eventPageRequest);
}
