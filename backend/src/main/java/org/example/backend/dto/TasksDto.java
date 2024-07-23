package org.example.backend.dto;

import org.example.backend.model.TasksStatusModel;

public record TasksDto(
        String title,
        String description,
        TasksStatusModel taskStatus,
        String dueDate,
        String assignedTo
) {
}
