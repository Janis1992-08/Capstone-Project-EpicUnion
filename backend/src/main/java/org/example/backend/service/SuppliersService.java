package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.SuppliersDto;
import org.example.backend.model.SuppliersModel;
import org.example.backend.model.TasksModel;
import org.example.backend.repository.SuppliersRepo;

import org.example.backend.repository.TasksRepo;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

import java.util.Optional;

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



    public void addSupplier(SuppliersDto suppliersDto, String userId) {
        SuppliersModel supplier = new SuppliersModel(
                idService.generateUUID(),
                suppliersDto.name(),
                suppliersDto.description(),
                suppliersDto.websiteUrl(),
                suppliersDto.costs(),
                suppliersDto.deliveryDate(),
                suppliersDto.assignedTasks() != null ? suppliersDto.assignedTasks() : new ArrayList<>(),
                suppliersDto.contactEmail(),
                suppliersDto.contactPhone(),
                suppliersDto.contactAddress(),
                userId
        );
        supplierRepo.save(supplier);
    }


    public void updateSupplier(String id, SuppliersDto suppliersDto, String userId) {
        SuppliersModel updateSupplier = supplierRepo.findByIdAndOwnerId(id, userId).orElseThrow();
        updateSupplier = updateSupplier.withName(suppliersDto.name())
                .withDescription(suppliersDto.description())
                .withWebsiteUrl(suppliersDto.websiteUrl())
                .withCosts(suppliersDto.costs())
                .withDeliveryDate(suppliersDto.deliveryDate())
                .withAssignedTasks(suppliersDto.assignedTasks() != null ? suppliersDto.assignedTasks() : new ArrayList<>())
                .withContactEmail(suppliersDto.contactEmail())
                .withContactPhone(suppliersDto.contactPhone())
                .withContactAddress(suppliersDto.contactAddress());
        supplierRepo.save(updateSupplier);
    }


    public void deleteSupplier(String id, String userId) {
        SuppliersModel supplier = supplierRepo.findByIdAndOwnerId(id, userId).orElseThrow();
        supplierRepo.delete(supplier);
    }

    public void assignTaskToSupplier(String supplierId, String taskId, String userId) {
        SuppliersModel supplier = supplierRepo.findByIdAndOwnerId(supplierId, userId).orElseThrow(() ->
                new IllegalArgumentException("Supplier not found or does not belong to the user"));

        List<String> updatedTasks = new ArrayList<>(supplier.assignedTasks());
        if (!updatedTasks.contains(taskId)) {
            updatedTasks.add(taskId);
            SuppliersModel updatedSupplier = supplier.withAssignedTasks(updatedTasks);
            supplierRepo.save(updatedSupplier);
        }

        TasksModel task = tasksRepo.findById(taskId).orElseThrow();
        List<String> updatedSuppliers = new ArrayList<>(task.assignedToSuppliers());
        if (!updatedSuppliers.contains(supplierId)) {
            updatedSuppliers.add(supplierId);
            TasksModel updatedTask = task.withAssignedToSuppliers(updatedSuppliers);
            tasksRepo.save(updatedTask);
        }
    }

    public void removeTaskFromSupplier(String supplierId, String taskId, String userId) {
        SuppliersModel supplier = supplierRepo.findByIdAndOwnerId(supplierId, userId).orElseThrow(() ->
                new IllegalArgumentException("Guest not found or does not belong to the user"));

        List<String> updatedTasks = new ArrayList<>(supplier.assignedTasks());
        if (updatedTasks.contains(taskId)) {
            updatedTasks.remove(taskId);
            SuppliersModel updatedSupplier = supplier.withAssignedTasks(updatedTasks);
            supplierRepo.save(updatedSupplier);
        }

        TasksModel task = tasksRepo.findById(taskId).orElseThrow();
        List<String> updatedSuppliers = new ArrayList<>(task.assignedToSuppliers());
        if (updatedSuppliers.contains(supplierId)) {
            updatedSuppliers.remove(supplierId);
            TasksModel updatedTask = task.withAssignedToSuppliers(updatedSuppliers);
            tasksRepo.save(updatedTask);
        }
    }




}

