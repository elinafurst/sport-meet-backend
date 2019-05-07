package se.elfu.sportprojectbackend.utils.converter;

import se.elfu.sportprojectbackend.controller.model.events.comments.CommentCreationDto;
import se.elfu.sportprojectbackend.controller.model.events.comments.CommentDto;
import se.elfu.sportprojectbackend.repository.model.Comment;
import se.elfu.sportprojectbackend.repository.model.Event;
import se.elfu.sportprojectbackend.repository.model.User;
import se.elfu.sportprojectbackend.utils.DateTimeParser;
import se.elfu.sportprojectbackend.utils.KeyValueMapper;

import java.time.LocalDateTime;
import java.util.UUID;

public final class CommentConverter {


    public static Comment createComment(CommentCreationDto entity, Event event, User user) {
        return Comment.builder()
                .commentNumber(UUID.randomUUID())
                .comment(entity.getComment())
                .timeStamp(LocalDateTime.now()) //TODO sweeden time
                .user(user)
                .event(event)
                .build();
    }

    public static CommentDto createCommentDto(Comment entity, boolean owner) {
        return CommentDto.builder()
                .commentNumber(entity.getCommentNumber())
                .comment(entity.getComment())
                .date(DateTimeParser.formatDate(entity.getTimeStamp()))
                .time(DateTimeParser.formatTime(entity.getTimeStamp()))
                .by(KeyValueMapper.mapUser(entity.getUser()))
                .owner(owner)
                .build();
    }
}
