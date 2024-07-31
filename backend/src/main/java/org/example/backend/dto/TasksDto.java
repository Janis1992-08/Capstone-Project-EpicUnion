package org.example.backend.dto;

import org.example.backend.model.TasksStatusModel;

import java.util.List;

public record TasksDto(
        String title,
        String description,
        TasksStatusModel taskStatus,
        String dueDate,
        List<String> assignedTo
) {}
