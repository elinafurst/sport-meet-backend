package se.elfu.sportprojectbackend.utils.converter;

import se.elfu.sportprojectbackend.controller.model.events.requests.RequestCreationDto;
import se.elfu.sportprojectbackend.controller.model.events.requests.messages.MessageDto;
import se.elfu.sportprojectbackend.repository.model.Message;
import se.elfu.sportprojectbackend.repository.model.User;
import se.elfu.sportprojectbackend.utils.DateTimeParser;
import se.elfu.sportprojectbackend.utils.KeyValueMapper;
import se.elfu.sportprojectbackend.utils.Validator;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public final class MessageConverter {

    public static Message createMessage(RequestCreationDto requestCreationDto, User activeUser, User reader) {
        return Message.builder()
                .messageNumber(UUID.randomUUID())
                .message(requestCreationDto.getMessage())
                .timeStamp(LocalDateTime.now())
                .author(activeUser)
                .reader(reader)
                .isRead(false)
                .build();
    }

    public static List<MessageDto> createMessageDtoList(Set<Message> messages, User user){
        return messages.stream()
                .sorted(Comparator.comparing(Message::getTimeStamp))
                .map(message -> createMessageDto(message, user))
                .collect(Collectors.toList());
    }

    public static MessageDto createMessageDto(Message message, User user) {
        return MessageDto.builder()
                .messageNumber(message.getMessageNumber())
                .message(message.getMessage())
                .timeStamp(DateTimeParser.formatDateTime(message.getTimeStamp()))
                .author(KeyValueMapper.mapUser(message.getAuthor()))
                .isAuthorOfMessage(Validator.isSameUser(message.getAuthor().getId(), user.getId()))
                .reader(KeyValueMapper.mapUser(message.getReader()))
                .isRead(message.isRead())
                .build();

    }

    public static MessageDto createMessageDto(Set<Message> messages, User user) {
        return messages.stream()
                .sorted(Comparator.comparing(Message::getTimeStamp, Comparator.reverseOrder()))
                .findFirst()
                .map(message -> createMessageDto(message, user))
                .orElse(null);
    }
}
