package org.example.backend.model;

import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@With
@Document(collection = "suppliers")
public record SuppliersModel(
        @Id
        String id,
        String name,
        String description,
        String websiteUrl,
        Double costs,
        String deliveryDate,
        List<String> assignedTasks,
        List<String> contactInfo,
        String ownerId
) {}
