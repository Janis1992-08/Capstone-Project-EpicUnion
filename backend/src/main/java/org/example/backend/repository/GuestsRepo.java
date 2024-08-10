package org.example.backend.repository;


import org.example.backend.model.GuestsModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuestsRepo extends MongoRepository <GuestsModel, String> {
    List<GuestsModel> findByOwnerId(String ownerId);
    Optional<GuestsModel> findByIdAndOwnerId(String id, String ownerId);
}
