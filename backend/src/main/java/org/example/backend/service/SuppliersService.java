package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.SuppliersDto;
import org.example.backend.model.SuppliersModel;
import org.example.backend.model.TasksModel;
import org.example.backend.repository.SuppliersRepo;

import org.example.backend.repository.TasksRepo;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
@RequiredArgsConstructor
public class SuppliersService {

    private final SuppliersRepo supplierRepo;
    private final TasksRepo tasksRepo;
    private final IdService idService;


    public List<SuppliersModel> getAllSuppliers(String userId) {
        return supplierRepo.findByOwnerId(userId);
    }

    public Optional<SuppliersModel> getSupplierById(String id, String userId) {
        return supplierRepo.findByIdAndOwnerId(id, userId);
    }




    public SuppliersModel addSupplier(SuppliersDto suppliersDto, String userId) {
        SuppliersModel supplier = new SuppliersModel(
                idService.generateUUID(),
                suppliersDto.name(),
                suppliersDto.description(),
                suppliersDto.websiteUrl(),
                suppliersDto.costs(),
                suppliersDto.deliveryDate(),
                suppliersDto.assignedTasks(),
                suppliersDto.contactEmail(),
                suppliersDto.contactPhone(),
                suppliersDto.contactAddress(),
                userId);

        supplierRepo.save(supplier);

        for (String taskId : suppliersDto.assignedTasks()) {
            TasksModel task = tasksRepo.findById(taskId).orElseThrow();
            List<String> updatedSuppliers = new ArrayList<>(task.assignedToSuppliers());
            updatedSuppliers.add(supplier.id());
            TasksModel updatedTask = task.withAssignedToSuppliers(updatedSuppliers);
            tasksRepo.save(updatedTask);
        }

        return supplier;
    }



    public SuppliersModel updateSupplier(String supplierId, SuppliersDto suppliersDto, String userId) {
        SuppliersModel updateSupplier = supplierRepo.findByIdAndOwnerId(supplierId, userId).orElseThrow();
        List<String> oldAssignedTo = updateSupplier.assignedTasks();

        updateSupplier = updateSupplier.withName(suppliersDto.name())
                .withDescription(suppliersDto.description())
                .withWebsiteUrl(suppliersDto.websiteUrl())
                .withCosts(suppliersDto.costs())
                .withDeliveryDate(suppliersDto.deliveryDate())
                .withAssignedTasks(suppliersDto.assignedTasks())
                .withContactEmail(suppliersDto.contactEmail())
                .withContactPhone(suppliersDto.contactPhone())
                .withContactAddress(suppliersDto.contactAddress());

        supplierRepo.save(updateSupplier);

        Set<String> newAssignedTo = new HashSet<>(suppliersDto.assignedTasks());
        Set<String> oldAssignedToSet = new HashSet<>(oldAssignedTo);

        for (String taskId : oldAssignedToSet) {
            if (!newAssignedTo.contains(taskId)) {
                TasksModel task = tasksRepo.findById(taskId).orElseThrow();
                List<String> updatedSuppliers = new ArrayList<>(task.assignedToSuppliers());
                updatedSuppliers.remove(updateSupplier.id());
                TasksModel updatedTask = task.withAssignedToSuppliers(updatedSuppliers);
                tasksRepo.save(updatedTask);
            }
        }

        for (String taskId : newAssignedTo) {
            if (!oldAssignedTo.contains(taskId)) {
                TasksModel task = tasksRepo.findById(taskId).orElseThrow();
                List<String> updatedSuppliers = new ArrayList<>(task.assignedToSuppliers());
                updatedSuppliers.add(updateSupplier.id());
                TasksModel updatedTask = task.withAssignedToSuppliers(updatedSuppliers);
                tasksRepo.save(updatedTask);
            }
        }

        return updateSupplier;
    }


    public void deleteSupplier(String supplierId, String userId) {
        SuppliersModel supplierToDelete = supplierRepo.findByIdAndOwnerId(supplierId, userId).orElseThrow();

        for (String taskId : supplierToDelete.assignedTasks()) {
            TasksModel task = tasksRepo.findById(taskId).orElseThrow();
            List<String> updatedSupplier = new ArrayList<>(task.assignedToSuppliers());
            updatedSupplier.remove(supplierToDelete.id());
            TasksModel updatedTask = task.withAssignedToSuppliers(updatedSupplier);
            tasksRepo.save(updatedTask);
        }

        supplierRepo.deleteById(supplierId);
    }

}

