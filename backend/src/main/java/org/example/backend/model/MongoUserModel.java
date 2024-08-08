package org.example.backend.model;

import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document(collection = "user")
public record MongoUserModel(
        String id,
        String username,
        String password
) {
}
