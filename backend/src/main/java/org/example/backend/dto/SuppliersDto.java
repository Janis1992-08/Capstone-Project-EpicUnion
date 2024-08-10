package org.example.backend.dto;


import java.util.List;

public record SuppliersDto(
        String name,
        String description,
        String websiteUrl,
        Double costs,
        String deliveryDate,
        List<String> assignedTasks,
        List<String> contactInfo,
        String ownerId
) {
}
