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
        String name,
        String email,
        RsvpStatusModel rsvpStatus,
        String notes,
        List<String> assignedTasks
) {}


