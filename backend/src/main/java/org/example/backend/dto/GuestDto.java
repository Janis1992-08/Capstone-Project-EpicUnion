package org.example.backend.dto;

import org.example.backend.model.RsvpStatus;

public record GuestDto(
        String name,
        String email,
        RsvpStatus rsvpStatus,
        String notes
) {}
