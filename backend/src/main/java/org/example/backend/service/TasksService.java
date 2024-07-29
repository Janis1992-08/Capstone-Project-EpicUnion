package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.TasksDto;
import org.example.backend.model.GuestsModel;
import org.example.backend.model.TasksModel;
import org.example.backend.repository.GuestsRepo;
import org.example.backend.repository.TasksRepo;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class TasksService {
    private final TasksRepo tasksRepo;
    private final GuestsRepo guestsRepo;
    private final IdService idService;


    public List<TasksModel> getAllTasks() {
        return tasksRepo.findAll();
    }

    public Optional<TasksModel> getTaskById(String id) {
        return tasksRepo.findById(id);
    }

    public TasksModel addTask(TasksDto tasksDto) {
        TasksModel tasks = new TasksModel(idService.generateUUID(),
                tasksDto.title(),
                tasksDto.description(),
                tasksDto.taskStatus(),
                tasksDto.dueDate(),
                tasksDto.assignedTo());

        tasksRepo.save(tasks);

        for (String guestId : tasksDto.assignedTo()) {
            GuestsModel guest = guestsRepo.findById(guestId).orElseThrow();
            List<String> updatedTasks = new ArrayList<>(guest.assignedTasks());
            updatedTasks.add(tasks.id());
            GuestsModel updatedGuest = guest.withAssignedTasks(updatedTasks);
            guestsRepo.save(updatedGuest);
        }

        return tasks;
    }

    public TasksModel updateTask(String id, TasksDto tasksDto) {
        TasksModel updateTask = tasksRepo.findById(id).orElseThrow();
        List<String> oldAssignedTo = updateTask.assignedTo();

        updateTask = updateTask.withTitle(tasksDto.title())
                .withDescription(tasksDto.description())
                .withTaskStatus(tasksDto.taskStatus())
                .withDueDate(tasksDto.dueDate())
                .withAssignedTo(tasksDto.assignedTo());

        tasksRepo.save(updateTask);

        Set<String> newAssignedTo = new HashSet<>(tasksDto.assignedTo());
        Set<String> oldAssignedToSet = new HashSet<>(oldAssignedTo);

        for (String guestId : oldAssignedToSet) {
            if (!newAssignedTo.contains(guestId)) {
                GuestsModel guest = guestsRepo.findById(guestId).orElseThrow();
                List<String> updatedTasks = new ArrayList<>(guest.assignedTasks());
                updatedTasks.remove(updateTask.id());
                GuestsModel updatedGuest = guest.withAssignedTasks(updatedTasks);
                guestsRepo.save(updatedGuest);
            }
        }

        for (String guestId : newAssignedTo) {
            if (!oldAssignedTo.contains(guestId)) {
                GuestsModel guest = guestsRepo.findById(guestId).orElseThrow();
                List<String> updatedTasks = new ArrayList<>(guest.assignedTasks());
                updatedTasks.add(updateTask.id());
                GuestsModel updatedGuest = guest.withAssignedTasks(updatedTasks);
                guestsRepo.save(updatedGuest);
            }
        }

        return updateTask;
    }


    public void deleteTask(String id) {
        TasksModel taskToDelete = tasksRepo.findById(id).orElseThrow();

        for (String guestId : taskToDelete.assignedTo()) {
            GuestsModel guest = guestsRepo.findById(guestId).orElseThrow();
            List<String> updatedTasks = new ArrayList<>(guest.assignedTasks());
            updatedTasks.remove(taskToDelete.id());
            GuestsModel updatedGuest = guest.withAssignedTasks(updatedTasks);
            guestsRepo.save(updatedGuest);
        }

        tasksRepo.deleteById(id);
    }


}
