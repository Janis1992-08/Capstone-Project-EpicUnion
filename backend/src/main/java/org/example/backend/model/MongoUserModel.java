package org.example.backend.model;

import lombok.Builder;

@Builder
public record MongoUserModel(
        String id,
        String username,
        String password
) {
}
