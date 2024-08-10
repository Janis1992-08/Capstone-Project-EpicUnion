package org.example.backend.dto;

import org.example.backend.model.RsvpStatusModel;

import java.util.List;

public record GuestDto(
        String name,
        String email,
        RsvpStatusModel rsvpStatus,
        String notes,
        List<String> assignedTasks,
        String ownerId
) {}


