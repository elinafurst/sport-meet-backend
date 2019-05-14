package se.elfu.sportprojectbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.elfu.sportprojectbackend.controller.model.events.requests.RequestCreationDto;
import se.elfu.sportprojectbackend.controller.model.events.requests.RequestDto;
import se.elfu.sportprojectbackend.controller.model.events.requests.RequestPreviewDto;
import se.elfu.sportprojectbackend.controller.params.Param;
import se.elfu.sportprojectbackend.repository.EventRepository;
import se.elfu.sportprojectbackend.repository.MessageRepository;
import se.elfu.sportprojectbackend.repository.RequestRepository;
import se.elfu.sportprojectbackend.repository.model.*;
import se.elfu.sportprojectbackend.service.helper.EmailSender;
import se.elfu.sportprojectbackend.service.helper.EntityRepositoryHelper;
import se.elfu.sportprojectbackend.utils.Validator;
import se.elfu.sportprojectbackend.utils.converter.MessageConverter;
import se.elfu.sportprojectbackend.utils.converter.RequestConverter;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RequestService {

    @Autowired
    private Validator validator;
    @Autowired
    private EmailSender emailSender;

    private final RequestRepository requestRepository;
    private final MessageRepository messageRepository;
    private final EntityRepositoryHelper entityRepositoryHelper;
    private final EventRepository eventRepository;

    public RequestService(RequestRepository requestRepository, MessageRepository messageRepository, EntityRepositoryHelper entityRepositoryHelper, EventRepository eventRepository) {
        this.requestRepository = requestRepository;
        this.messageRepository = messageRepository;
        this.entityRepositoryHelper = entityRepositoryHelper;
        this.eventRepository = eventRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public UUID joinEventRequest(UUID eventNumber, RequestCreationDto requestCreationDto) {
        User user = entityRepositoryHelper.getActiveUser();
        Event event = entityRepositoryHelper.getEvent(eventNumber);
        User eventOwner = event.getCreatedBy();

        Validator.isCreatorOfEvent(user, eventOwner);
        Validator.isEventActive(event);
        validator.isRequestAlreadySent(event, user);

        Message message = messageRepository.save(MessageConverter.createMessage(requestCreationDto, user, eventOwner));
        Request request = RequestConverter.createRequest(user, eventOwner, message, event);
        request = requestRepository.save(request);

        emailSender.sendNewRequestNotification(request);

        return request.getRequestNumber();
    }

    public List<RequestPreviewDto> getEventRequests(Param param){
        User user = entityRepositoryHelper.getActiveUser();

        return requestRepository.findDistinctBySenderOrReceiver(user, user, param.getRequestPageRequest())
                .stream()
                .map(request -> RequestConverter.createRequestPreviewDto(user, request))
                .sorted(Comparator.comparing(request -> request.getMessage().getTimeStamp(), Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    public RequestDto getEventRequest(UUID requestNumber) {
        User user = entityRepositoryHelper.getActiveUser();
        Request request = entityRepositoryHelper.getRequest(requestNumber);

        return RequestConverter.createRequestDto(request, user);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveMessageForRequest(UUID requestNumber, RequestCreationDto requestCreationDto) {
        User user = entityRepositoryHelper.getActiveUser();
        Request request = entityRepositoryHelper.getRequest(requestNumber);
        User reader = getOppositeUserInConversation(user, request);

        Message message = messageRepository.save(MessageConverter.createMessage(requestCreationDto, user, reader));
        request.addMessage(message);

        requestRepository.save(request);
    }

    @Transactional(rollbackFor = Exception.class)
    public void answerRequest(UUID requestNumber, boolean requestAnswer) {
        User user = entityRepositoryHelper.getActiveUser();
        Request request = entityRepositoryHelper.getRequest(requestNumber);

        Validator.isReceiver(user.getId(), request.getReceiver().getId());
        RequestStatus requestStatus = RequestStatus.isAccepted(requestAnswer);

        requestRepository.save(RequestConverter.setRequestStatusUpdateFrom(request, requestStatus));
        addParticipantIfRequestAccepted(request, requestAnswer);
    }

    private void addParticipantIfRequestAccepted(Request request, boolean requestAnswer) {
        if(requestAnswer){
            Event event = entityRepositoryHelper.getEvent(request.getEvent().getEventNumber());
            Validator.isEventActive(event);
            event.addParticipant(request.getSender());
            eventRepository.save(event);
        }
    }

    private static User getOppositeUserInConversation(User activeUser, Request request) {
        return (activeUser.getId().equals(request.getSender().getId())) ? request.getReceiver() : request.getSender();
    }
}
