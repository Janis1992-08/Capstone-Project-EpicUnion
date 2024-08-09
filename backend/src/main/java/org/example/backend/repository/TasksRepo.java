package org.example.backend.repository;


import org.example.backend.model.TasksModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TasksRepo extends MongoRepository <TasksModel, String> {
    List<TasksModel> findAllByOwnerId(String ownerId);
    Optional<TasksModel> findByIdAndOwnerId(String id, String ownerId);
}
