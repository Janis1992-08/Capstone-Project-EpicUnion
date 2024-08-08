package org.example.backend.model;


import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@With
@Document(collection = "tasks")
public record TasksModel(
        @Id
        String id,
        String title,
        String description,
        TasksStatusModel taskStatus,
        String dueDate,
        List<String> assignedTo,
        String ownerId
) {}
