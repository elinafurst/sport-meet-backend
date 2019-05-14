package se.elfu.sportprojectbackend.repository.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Message {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @Column(columnDefinition = "BINARY(16)")
    private UUID messageNumber;
    @Column(length = 1000)
    private String message;
    private LocalDateTime timeStamp;
    @OneToOne()
    private User author;
    @OneToOne()
    private User reader;
    private boolean isRead;
}
