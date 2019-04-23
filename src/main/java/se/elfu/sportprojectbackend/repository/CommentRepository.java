package se.elfu.sportprojectbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import se.elfu.sportprojectbackend.repository.model.Comment;

import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    Optional<Comment> findByCommentNumber(UUID commentNumber);

    Page<Comment> findByEventEventNumber(UUID eventNumber, Pageable commentPageRequest);
}