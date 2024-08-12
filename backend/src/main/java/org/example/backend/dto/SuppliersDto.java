package org.example.backend.dto;


import java.util.List;

public record SuppliersDto(
        String name,
        String description,
        String websiteUrl,
        Double costs,
        String deliveryDate,
        List<String> assignedTasks,
        String contactEmail,
        String contactPhone,
        String contactAddress,
        String ownerId
) {
}
