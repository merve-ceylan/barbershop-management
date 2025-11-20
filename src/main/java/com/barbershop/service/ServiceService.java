package com.barbershop.service;

import com.barbershop.exception.ResourceNotFoundException;
import com.barbershop.model.dto.request.ServiceRequest;
import com.barbershop.model.dto.response.ServiceResponse;
import com.barbershop.model.entity.Service;
import com.barbershop.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Transactional
public class ServiceService {

    private final ServiceRepository serviceRepository;
    // TODO: Add ReviewRepository for average rating

    /**
     * Get all active services
     */
    public List<ServiceResponse> getAllActiveServices() {
        List<Service> services = serviceRepository.findByActiveTrueOrderByNameAsc();
        return services.stream()
                .map(ServiceResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get all services (including inactive) - Admin only
     */
    public List<ServiceResponse> getAllServices() {
        List<Service> services = serviceRepository.findAll();
        return services.stream()
                .map(ServiceResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get service by ID
     */
    public Service findById(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service", "id", id));
    }

    /**
     * Get service response by ID
     */
    public ServiceResponse getServiceById(Long id) {
        Service service = findById(id);
        ServiceResponse response = ServiceResponse.fromEntity(service);

        // TODO: Calculate and set average rating from reviews
        // Double avgRating = reviewRepository.getAverageRatingForService(service);
        // response.setAverageRating(avgRating);

        return response;
    }

    /**
     * Create new service
     */
    public ServiceResponse createService(ServiceRequest request) {
        Service service = new Service();
        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setDurationMinutes(request.getDurationMinutes());
        service.setPrice(request.getPrice());
        service.setImageUrl(request.getImageUrl());
        service.setActive(request.getActive() != null ? request.getActive() : true);

        Service savedService = serviceRepository.save(service);
        return ServiceResponse.fromEntity(savedService);
    }

    /**
     * Update existing service
     */
    public ServiceResponse updateService(Long id, ServiceRequest request) {
        Service service = findById(id);

        // Update fields
        if (request.getName() != null) {
            service.setName(request.getName());
        }
        if (request.getDescription() != null) {
            service.setDescription(request.getDescription());
        }
        if (request.getDurationMinutes() != null) {
            service.setDurationMinutes(request.getDurationMinutes());
        }
        if (request.getPrice() != null) {
            service.setPrice(request.getPrice());
        }
        if (request.getImageUrl() != null) {
            service.setImageUrl(request.getImageUrl());
        }
        if (request.getActive() != null) {
            service.setActive(request.getActive());
        }

        Service updatedService = serviceRepository.save(service);
        return ServiceResponse.fromEntity(updatedService);
    }

    /**
     * Delete service (soft delete - set inactive)
     */
    public void deleteService(Long id) {
        Service service = findById(id);
        service.setActive(false);
        serviceRepository.save(service);
    }

    /**
     * Permanently delete service (hard delete)
     */
    public void permanentlyDeleteService(Long id) {
        Service service = findById(id);
        serviceRepository.delete(service);
    }

    /**
     * Activate service
     */
    public void activateService(Long id) {
        Service service = findById(id);
        service.setActive(true);
        serviceRepository.save(service);
    }
}
