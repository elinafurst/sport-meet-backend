package se.elfu.sportprojectbackend.repository.model;

public enum RequestStatus {
    PENDING, ACCEPTED, DENIED, LEFT, CANCELLED;

    public static RequestStatus isAccepted(boolean accepted) {
        return (accepted) ? ACCEPTED : DENIED;
    }
}
