package com.pwa.helpdesk.entity;

import com.pwa.helpdesk.enums.Priority;
import com.pwa.helpdesk.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "help_desk_tickets")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String summary;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(length = 1000)
    private String description;

    private String category;

    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    @PrePersist
    void prevSave() {
        if(this.createdOn == null) {
            this.createdOn = LocalDateTime.now();
        }
        this.updatedOn = LocalDateTime.now();
    }

    @PreUpdate
    void prevUpdate() {
        this.updatedOn = LocalDateTime.now();
    }

}
