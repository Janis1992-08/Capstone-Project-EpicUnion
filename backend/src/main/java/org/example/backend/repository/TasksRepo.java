package org.example.backend.repository;


import org.example.backend.model.TasksModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TasksRepo extends MongoRepository <TasksModel, String> {
}
