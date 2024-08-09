package org.example.backend.repository;

import org.example.backend.model.MongoUserModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface MongoUserRepo extends MongoRepository<MongoUserModel, String> {
    Optional<MongoUserModel> findByUsername(String username);
}
