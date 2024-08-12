package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.TasksDto;
import org.example.backend.model.GuestsModel;
import org.example.backend.model.SuppliersModel;
import org.example.backend.model.TasksModel;
import org.example.backend.repository.GuestsRepo;
import org.example.backend.repository.SuppliersRepo;
import org.example.backend.repository.TasksRepo;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class TasksService {
    private final TasksRepo tasksRepo;
    private final GuestsRepo guestsRepo;
    private final SuppliersRepo suppliersRepo;
    private final IdService idService;


    public List<TasksModel> getAllTasks(String userId) {
        return tasksRepo.findAllByOwnerId(userId);
    }

    public Optional<TasksModel> getTaskById(String taskId, String userId) {
        return tasksRepo.findByIdAndOwnerId(taskId, userId);
    }

    public TasksModel addTask(TasksDto tasksDto, String userId) {
        TasksModel task = new TasksModel(idService.generateUUID(),
                tasksDto.title(),
                tasksDto.description(),
                tasksDto.taskStatus(),
                tasksDto.dueDate(),
                tasksDto.assignedToGuests(),
                tasksDto.assignedToSuppliers(),
                userId);

        tasksRepo.save(task);

        for (String guestId : tasksDto.assignedToGuests()) {
            GuestsModel guest = guestsRepo.findById(guestId).orElseThrow();
            List<String> updatedTasks = new ArrayList<>(guest.assignedTasks());
            updatedTasks.add(task.id());
            GuestsModel updatedGuest = guest.withAssignedTasks(updatedTasks);
            guestsRepo.save(updatedGuest);
        }

        for (String supplierId : tasksDto.assignedToSuppliers()) {
            SuppliersModel supplier = suppliersRepo.findById(supplierId).orElseThrow(() -> new NoSuchElementException("Supplier not found."));
            List<String> updatedTasks = new ArrayList<>(supplier.assignedTasks());
            updatedTasks.add(task.id());
            supplier = supplier.withAssignedTasks(updatedTasks);
            suppliersRepo.save(supplier);
        }

        return task;
    }

    public TasksModel updateTask(String taskId, TasksDto tasksDto, String userId) {
        TasksModel updateTask = tasksRepo.findByIdAndOwnerId(taskId, userId)
                .orElseThrow(() -> new NoSuchElementException("Task not found or not authorized to access"));

        List<String> oldAssignedGuests = new ArrayList<>(updateTask.assignedToGuests());
        List<String> oldAssignedSuppliers = new ArrayList<>(updateTask.assignedToSuppliers());

        updateTask = updateTask.withTitle(tasksDto.title())
                .withDescription(tasksDto.description())
                .withTaskStatus(tasksDto.taskStatus())
                .withDueDate(tasksDto.dueDate())
                .withAssignedToGuests(tasksDto.assignedToGuests())
                .withAssignedToSuppliers(tasksDto.assignedToSuppliers());

        tasksRepo.save(updateTask);

        updateAssignments(oldAssignedGuests, tasksDto.assignedToGuests(), updateTask, true);
        updateAssignments(oldAssignedSuppliers, tasksDto.assignedToSuppliers(), updateTask, false);

        return updateTask;
    }

    private void updateAssignments(List<String> oldAssigned, List<String> newAssigned, TasksModel task, boolean isGuests) {
        Set<String> oldAssignedSet = new HashSet<>(oldAssigned);
        Set<String> newAssignedSet = new HashSet<>(newAssigned);

        oldAssignedSet.stream()
                .filter(id -> !newAssignedSet.contains(id))
                .forEach(id -> removeAssignment(id, task, isGuests));

        newAssignedSet.stream()
                .filter(id -> !oldAssignedSet.contains(id))
                .forEach(id -> addAssignment(id, task, isGuests));
    }

    private void removeAssignment(String id, TasksModel task, boolean isGuests) {
        if (isGuests) {
            GuestsModel guest = guestsRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Guest not found"));
            guest = guest.withAssignedTasks(guest.assignedTasks().stream().filter(tid -> !tid.equals(task.id())).collect(Collectors.toList()));
            guestsRepo.save(guest);
        } else {
            SuppliersModel supplier = suppliersRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Supplier not found"));
            supplier = supplier.withAssignedTasks(supplier.assignedTasks().stream().filter(tid -> !tid.equals(task.id())).collect(Collectors.toList()));
            suppliersRepo.save(supplier);
        }
    }

    private void addAssignment(String id, TasksModel task, boolean isGuests) {
        if (isGuests) {
            GuestsModel guest = guestsRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Guest not found"));
            guest = guest.withAssignedTasks(Stream.concat(guest.assignedTasks().stream(), Stream.of(task.id())).collect(Collectors.toList()));
            guestsRepo.save(guest);
        } else {
            SuppliersModel supplier = suppliersRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Supplier not found"));
            supplier = supplier.withAssignedTasks(Stream.concat(supplier.assignedTasks().stream(), Stream.of(task.id())).collect(Collectors.toList()));
            suppliersRepo.save(supplier);
        }
    }


    public void deleteTask(String taskId, String userId) {
        TasksModel taskToDelete = tasksRepo.findByIdAndOwnerId(taskId, userId).orElseThrow();

        for (String guestId : taskToDelete.assignedToGuests()) {
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

        if (!task.assignedToGuests().contains(guestId)) {
            List<String> newAssignedGuests = new ArrayList<>(task.assignedToGuests());
            newAssignedGuests.add(guestId);
            TasksModel updatedTask = task.withAssignedToGuests(newAssignedGuests);
            tasksRepo.save(updatedTask);
        } else {
            throw new IllegalStateException("Guest is already assigned to this task.");
        }
    }

    public void addSupplierToTask(String taskId, String supplierId, String userId) {
        TasksModel task = tasksRepo.findByIdAndOwnerId(taskId, userId)
                .orElseThrow(() -> new NoSuchElementException("Task not found or you're not authorized to modify this task"));

        if (!task.assignedToSuppliers().contains(supplierId)) {
            List<String> newAssignedSuppliers = new ArrayList<>(task.assignedToSuppliers());
            newAssignedSuppliers.add(supplierId);
            TasksModel updatedTask = task.withAssignedToSuppliers(newAssignedSuppliers);
            tasksRepo.save(updatedTask);
        } else {
            throw new IllegalStateException("Supplier is already assigned to this task.");
        }
    }

}
