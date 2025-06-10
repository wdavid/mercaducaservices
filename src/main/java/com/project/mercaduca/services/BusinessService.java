package com.project.mercaduca.services;

import com.project.mercaduca.dtos.BusinessRequestDTO;
import com.project.mercaduca.dtos.BusinessUpdateDTO;
import com.project.mercaduca.models.Business;
import com.project.mercaduca.models.User;
import com.project.mercaduca.repositories.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusinessService {

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private AuthService authService;

    public void updateBusiness(BusinessUpdateDTO dto) {
        User currentUser = authService.getAuthenticatedUser();
        Business business = businessRepository.findByOwner(currentUser)
                .orElseThrow(() -> new RuntimeException("No se encontró negocio del usuario"));

        business.setPriceRange(dto.getPriceRange());
        business.setFacebook(dto.getFacebook());
        business.setInstagram(dto.getInstagram());
        business.setPhone(dto.getPhone());

        if (dto.getUrlLogo() != null) {
            business.setUrlLogo(dto.getUrlLogo());
        }

        businessRepository.save(business);
    }

    public BusinessRequestDTO getBusinessOfAuthenticatedUser() {
        User currentUser = authService.getAuthenticatedUser();
        Business business = businessRepository.findByOwner(currentUser)
                .orElseThrow(() -> new RuntimeException("No se encontró negocio del usuario"));

        BusinessRequestDTO dto = new BusinessRequestDTO();
        dto.setId(business.getId());
        dto.setBusinessName(business.getBusinessName());
        dto.setDescription(business.getDescription());
        dto.setSector(business.getSector());
        dto.setProductType(business.getProductType());
        dto.setPriceRange(business.getPriceRange());
        dto.setFacebook(business.getFacebook());
        dto.setInstagram(business.getInstagram());
        dto.setPhone(business.getPhone());
        dto.setUrlLogo(business.getUrlLogo());

        return dto;
    }

}
