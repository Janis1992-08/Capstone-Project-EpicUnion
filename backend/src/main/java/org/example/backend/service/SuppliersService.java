package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.SuppliersDto;
import org.example.backend.model.SuppliersModel;
import org.example.backend.repository.SuppliersRepo;

import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SuppliersService {

    private final SuppliersRepo supplierRepo;
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
                suppliersDto.contactInfo() != null ? suppliersDto.contactInfo() : new ArrayList<>(),
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
                .withContactInfo(suppliersDto.contactInfo() != null ? suppliersDto.contactInfo() : new ArrayList<>());
        supplierRepo.save(updateSupplier);
    }


    public void deleteSupplier(String id, String userId) {
        SuppliersModel supplier = supplierRepo.findByIdAndOwnerId(id, userId).orElseThrow();
        supplierRepo.delete(supplier);
    }




}

