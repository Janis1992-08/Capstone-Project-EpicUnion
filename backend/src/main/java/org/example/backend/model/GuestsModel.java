package org.example.backend.model;


import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@With
@Document(collection = "guests")
public record GuestsModel(
        @Id
        String id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        RsvpStatusModel rsvpStatus,
        String notes,
        List<String> assignedTasks,
        String ownerId
) {}


