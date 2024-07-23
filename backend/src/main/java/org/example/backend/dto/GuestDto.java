package org.example.backend.dto;

import org.example.backend.model.RsvpStatusModel;

public record GuestDto(
        String name,
        String email,
        RsvpStatusModel rsvpStatus,
        String notes
) {}


