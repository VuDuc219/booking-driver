package com.fpt.booking.domain.entities;

import com.fpt.booking.domain.enums.RequestTicketType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "firebase_notification")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FirebaseNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "sent_date", nullable = false)
    private LocalDateTime sentDate;

    @Column(name = "sender", nullable = false)
    private Long sender;

    @Column(name = "receiver")
    private Long receiver;

    @Column(name = "viewed")
    private Boolean viewed;

    @Column(name = "subject")
    private String subject;

    @Column(name = "content")
    private String content;

    @ElementCollection
    @CollectionTable(name = "firebase_notification_data",
            joinColumns = @JoinColumn(name = "firebase_notification_id", referencedColumnName = "id"))
    @MapKeyColumn(name = "data_key")
    @Column(name = "data_content")
    private Map<String, String> data;

    @Column(name = "image")
    private String image;

    public FirebaseNotification(LocalDateTime sentDate, Long sender, String subject, String content) {
        this.sentDate = sentDate;
        this.sender = sender;
        this.subject = subject;
        this.content = content;
    }

    public FirebaseNotification(LocalDateTime sentDate, Long sender, String subject, String content, Map<String, String> data) {
        this.sentDate = sentDate;
        this.sender = sender;
        this.subject = subject;
        this.content = content;
        this.data = data;
    }

    public FirebaseNotification(LocalDateTime sentDate, Long sender, String subject, String content, String image) {
        this.sentDate = sentDate;
        this.sender = sender;
        this.subject = subject;
        this.content = content;
        this.image = image;
    }

    public FirebaseNotification(LocalDateTime sentDate, Long sender, String subject, String content, String image, Map<String, String> data) {
        this.sentDate = sentDate;
        this.sender = sender;
        this.subject = subject;
        this.content = content;
        this.image = image;
        this.data = data;
    }

}