package org.example.backend.model;


import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@With
@Document(collection = "guests")
public record Guests(
        @Id
        String id,
        String name,
        String email,
        RsvpStatus rsvpStatus,
        String notes
) {}
