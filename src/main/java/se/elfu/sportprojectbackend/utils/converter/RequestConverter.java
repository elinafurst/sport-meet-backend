package se.elfu.sportprojectbackend.utils.converter;

import se.elfu.sportprojectbackend.controller.model.events.requests.RequestDto;
import se.elfu.sportprojectbackend.controller.model.events.requests.RequestPreviewDto;
import se.elfu.sportprojectbackend.repository.model.*;
import se.elfu.sportprojectbackend.utils.KeyValueMapper;
import se.elfu.sportprojectbackend.utils.Validator;

import java.util.*;
import java.util.stream.Collectors;

public final class RequestConverter {

    public static Request createRequest(User user, User eventOwner, Message message, Event event) {
        return Request.builder()
                .requestNumber(UUID.randomUUID())
                .event(event)
                .receiver(eventOwner)
                .sender(user)
                .requestStatus(RequestStatus.PENDING)
                .messages(new HashSet<>(Arrays.asList(message)))
                .build();
    }

    public static List<RequestDto> createRequestDtoList(List<Request> requests, User user) {
        return requests.stream()
                .map(request -> RequestConverter.createRequestDto(request, user))
                .collect(Collectors.toList());
    }

    public static RequestDto createRequestDto(Request request, User user) {
        return RequestDto.builder()
                .requestNumber(request.getRequestNumber())
                .event(KeyValueMapper.mapEvent(request.getEvent()))
                .receiver(KeyValueMapper.mapUser(request.getReceiver()))
                .sender(KeyValueMapper.mapUser(request.getSender()))
                .requestStatus(request.getRequestStatus())
                .messages(MessageConverter.createMessageDtoList(request.getMessages(), user))
                .isRequester(Validator.isSameUser(request.getSender().getId(), user.getId()))
                .build();
    }

    public static Request setRequestStatusUpdateFrom(Request request, RequestStatus requestStatus) {
        return request.toBuilder()
                .requestStatus(requestStatus)
                .build();
    }

    public static RequestPreviewDto createRequestPreviewDto(User user, Request request) {
        return RequestPreviewDto.builder()
                .requestNumber(request.getRequestNumber())
                .event(KeyValueMapper.mapEvent(request.getEvent()))
                .receiver(KeyValueMapper.mapUser(request.getReceiver()))
                .sender(KeyValueMapper.mapUser(request.getSender()))
                .requestStatus(request.getRequestStatus())
                .message(MessageConverter.createMessageDto(request.getMessages(), user))
                .isRequester(Validator.isSameUser(request.getSender().getId(), user.getId()))
                .build();
    }
}
