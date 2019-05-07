package se.elfu.sportprojectbackend.utils.converter;

import se.elfu.sportprojectbackend.controller.model.MessageDto;
import se.elfu.sportprojectbackend.controller.model.RequestCreationDto;
import se.elfu.sportprojectbackend.controller.model.RequestDto;
import se.elfu.sportprojectbackend.controller.model.RequestPreviewDto;
import se.elfu.sportprojectbackend.repository.model.*;
import se.elfu.sportprojectbackend.utils.DateTimeParser;
import se.elfu.sportprojectbackend.utils.KeyValueMapper;
import se.elfu.sportprojectbackend.utils.Validator;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public final class RequestConverter {

    public static Message createFrom(RequestCreationDto requestCreationDto, User activeUser, User reader) {
        return Message.builder()
                .messageNumber(UUID.randomUUID())
                .message(requestCreationDto.getMessage())
                .timeStamp(LocalDateTime.now())
                .author(activeUser)
                .reader(reader)
                .isRead(false)
                .build();
    }

    public static Request createFrom(User user, User eventOwner, Message message, Event event) {
        return Request.builder()
                .requestNumber(UUID.randomUUID())
                .event(event)
                .receiver(eventOwner)
                .sender(user)
                .requestStatus(RequestStatus.PENDING)
                .messages(new HashSet<>(Arrays.asList(message)))
                .isRead(false)
                .build();
    }

    public static List<RequestDto> createFromEntities(List<Request> requests, User user) {
        return requests.stream()
                .map(request -> RequestConverter.createFrom(request, user))
                .collect(Collectors.toList());
    }

    public static RequestDto createFrom(Request request, User user) {
        return RequestDto.builder()
                .requestNumber(request.getRequestNumber())
                .event(KeyValueMapper.mapEvent(request.getEvent()))
                .receiver(KeyValueMapper.mapUser(request.getReceiver()))
                .sender(KeyValueMapper.mapUser(request.getSender()))
                .requestStatus(request.getRequestStatus())
                .messages(createFromEntities(request.getMessages(), user))
                .isRequester(Validator.isSameUser(request.getSender().getId(), user.getId()))
                .isRead(request.isRead())
                .build();
    }

    private static List<MessageDto> createFromEntities(Set<Message> messages, User user){
        return messages.stream()
                .sorted(Comparator.comparing(Message::getTimeStamp))
                .map(message -> RequestConverter.createFrom(message, user))
                .collect(Collectors.toList());
    }

    private static MessageDto createFrom(Message message, User user) {
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

    public static Request updateFrom(Request request, RequestStatus requestStatus) {
        return request.toBuilder()
                .requestStatus(requestStatus)
                .build();
    }

    public static Request updateFrom(Request request) {
        return request.toBuilder()
                .isRead(true)
                .build();
    }

    public static RequestPreviewDto createFrom(User user, Request request) {
        return RequestPreviewDto.builder()
                .requestNumber(request.getRequestNumber())
                .event(KeyValueMapper.mapEvent(request.getEvent()))
                .receiver(KeyValueMapper.mapUser(request.getReceiver()))
                .sender(KeyValueMapper.mapUser(request.getSender()))
                .requestStatus(request.getRequestStatus())
                .message(createFromEntitiesSingle(request.getMessages(), user))
                .isRequester(Validator.isSameUser(request.getSender().getId(), user.getId()))
                .isRead(request.isRead())
                .build();
    }

    private static MessageDto createFromEntitiesSingle(Set<Message> messages, User user) {
        return messages.stream()
                .sorted(Comparator.comparing(Message::getTimeStamp, Comparator.reverseOrder()))
                .findFirst()
                .map(message -> RequestConverter.createFrom(message, user))
                .orElse(null);
    }
}
