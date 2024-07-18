package org.example.backend.repository;


import org.example.backend.model.Guests;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestsRepo extends MongoRepository <Guests, String> {
}
