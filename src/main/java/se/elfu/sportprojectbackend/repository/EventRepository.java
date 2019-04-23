package se.elfu.sportprojectbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;
import se.elfu.sportprojectbackend.repository.model.Event;
import se.elfu.sportprojectbackend.repository.model.Unit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRepository extends PagingAndSortingRepository<Event, Long>, QueryByExampleExecutor<Event> {

    Page<Event> findByEventStartBetweenAndActiveTrue(LocalDateTime parseDate, LocalDateTime parseDate1, PageRequest pageRequest);

    Page<Event> findByEventStartBetweenAndSportNameAndLocationCityAndActiveTrue(LocalDateTime parseDateTime, LocalDateTime parseDateTime1, String type, String city, PageRequest pageRequest);

    Page<Event> findByEventStartBetweenAndSportNameAndActiveTrue(LocalDateTime parseDateTime, LocalDateTime parseDateTime1, String type, PageRequest pageRequest);

    Page<Event> findByEventStartGreaterThanEqualAndSportNameAndLocationCityAndLocationAreaAreaAndActiveTrue(LocalDateTime parseDateTime, String type, String city, String area, PageRequest pageRequest);

    Page<Event> findByEventStartGreaterThanEqualAndSportNameAndLocationCityAndActiveTrue(LocalDateTime parseDateTime, String type, String city, PageRequest pageRequest);

    Page<Event> findByEventStartGreaterThanEqualAndSportNameAndActiveTrue(LocalDateTime parseDateTime, String type, PageRequest pageRequest);

    Page<Event> findByEventStartGreaterThanEqualAndActiveTrue(LocalDateTime parseDateTime, String type, PageRequest pageRequest);

    Page<Event> findByEventStartBetweenAndSportNameAndLocationCityAndLocationAreaAreaAndActiveTrue(LocalDateTime now, LocalDateTime parseDateTime, String type, String city, String area, PageRequest pageRequest);

    Page<Event> findByEventStartBetweenAndLocationCityAndActiveTrue(LocalDateTime now, LocalDateTime parseDateTime, String city, PageRequest pageRequest);

    Page<Event> findByEventStartGreaterThanEqualAndLocationCityAndActiveTrue(LocalDateTime parseDateTime, String city, PageRequest pageRequest);

    Optional<Event> findByEventNumber(UUID eventNumber);

    Page<Event> findByByUnit(Unit unitNumber, Pageable pageable);

    List<Event> findByEventStartLessThanAndActiveTrue(LocalDateTime expirationDateTime);
}
