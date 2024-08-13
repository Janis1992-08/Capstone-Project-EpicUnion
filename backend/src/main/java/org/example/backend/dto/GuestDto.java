package org.example.backend.dto;

import org.example.backend.model.RsvpStatusModel;

import java.util.List;

public record GuestDto(
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        RsvpStatusModel rsvpStatus,
        String notes,
        List<String> assignedTasks,
        String ownerId
) {}


