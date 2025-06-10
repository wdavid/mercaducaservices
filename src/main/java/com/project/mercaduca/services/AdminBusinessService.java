package com.project.mercaduca.services;

import com.project.mercaduca.models.Business;
import com.project.mercaduca.repositories.BusinessRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminBusinessService {
    @Autowired
    private BusinessRepository businessRepository;

    @Transactional
    public void deleteAnyBusiness(Long businessId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Negocio no encontrado"));

        business.setStatus("ELIMINADO");
        businessRepository.save(business);
    }
}
