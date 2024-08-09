package org.example.backend.dto;

public record MongoUserDto(
        String username,
        String password,
        String email
) {
}
