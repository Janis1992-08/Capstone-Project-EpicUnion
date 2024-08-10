package org.example.backend.repository;

import org.example.backend.model.GuestsModel;
import org.example.backend.model.SuppliersModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SuppliersRepo extends MongoRepository<SuppliersModel, String> {
    List<SuppliersModel> findByOwnerId(String ownerId);
    Optional<SuppliersModel> findByIdAndOwnerId(String id, String ownerId);

}
