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


    public List<TasksModel> getAllTasks(String userId) {
        return tasksRepo.findAllByOwnerId(userId);
    }

    public Optional<TasksModel> getTaskById(String taskId, String userId) {
        return tasksRepo.findByIdAndOwnerId(taskId, userId);
    }

    public TasksModel addTask(TasksDto tasksDto, String userId) {
        TasksModel tasks = new TasksModel(idService.generateUUID(),
                tasksDto.title(),
                tasksDto.description(),
                tasksDto.taskStatus(),
                tasksDto.dueDate(),
                tasksDto.assignedTo(),
                userId);

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

    public TasksModel updateTask(String taskId, TasksDto tasksDto, String userId) {
        TasksModel updateTask = tasksRepo.findByIdAndOwnerId(taskId, userId).orElseThrow();
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


    public void deleteTask(String taskId, String userId) {
        TasksModel taskToDelete = tasksRepo.findByIdAndOwnerId(taskId, userId).orElseThrow();

        for (String guestId : taskToDelete.assignedTo()) {
            GuestsModel guest = guestsRepo.findById(guestId).orElseThrow();
            List<String> updatedTasks = new ArrayList<>(guest.assignedTasks());
            updatedTasks.remove(taskToDelete.id());
            GuestsModel updatedGuest = guest.withAssignedTasks(updatedTasks);
            guestsRepo.save(updatedGuest);
        }

        tasksRepo.deleteById(taskId);
    }


    public void addGuestToTask(String taskId, String guestId, String userId) {
        TasksModel task = tasksRepo.findByIdAndOwnerId(taskId, userId)
                .orElseThrow(() -> new NoSuchElementException("Task not found or you're not authorized to modify this task"));

        if (!task.assignedTo().contains(guestId)) {
            List<String> newAssignedGuests = new ArrayList<>(task.assignedTo());
            newAssignedGuests.add(guestId);
            TasksModel updatedTask = task.withAssignedTo(newAssignedGuests);
            tasksRepo.save(updatedTask);
        } else {
            throw new IllegalStateException("Guest is already assigned to this task.");
        }
    }

}
