package com.project.mercaduca.services;

import com.project.mercaduca.dtos.BusinessApprovedDTO;
import com.project.mercaduca.dtos.ContractRequestDTO;
import com.project.mercaduca.models.Business;
import com.project.mercaduca.models.Contract;
import com.project.mercaduca.models.Payment;
import com.project.mercaduca.models.User;
import com.project.mercaduca.repositories.BusinessRepository;
import com.project.mercaduca.repositories.ContractRepository;
import com.project.mercaduca.repositories.PaymentRepository;
import com.project.mercaduca.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminBusinessService {
    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Transactional
    public void deleteAnyBusiness(Long businessId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Negocio no encontrado"));

        business.setStatus("ELIMINADO");
        businessRepository.save(business);
    }

    @Transactional
    public List<BusinessApprovedDTO> getFilteredBusinesses(List<String> statuses, Boolean tienenContrato) {
        List<Business> businesses = businessRepository.findByStatusesAndContrato(statuses, tienenContrato);

        return businesses.stream().map(business -> {
            User owner = business.getOwner();
            return new BusinessApprovedDTO(
                    business.getOwner().getId(),
                    business.getUrlLogo(),
                    business.getBusinessName(),
                    owner.getName() + " " + owner.getLastName(),
                    owner.getMail(),
                    owner.getMajor(),
                    owner.getFaculty()
            );
        }).collect(Collectors.toList());
    }

    @Transactional
    public ContractRequestDTO getContractById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Contract contract = contractRepository.findByUser(user).orElse(null);
        Payment payment = paymentRepository.findTopByUserOrderByDateDesc(user).orElse(null);

        ContractRequestDTO dto = new ContractRequestDTO();

        if (user.getBusiness() != null) {
            dto.setBusinessName(user.getBusiness().getBusinessName());
            dto.setPhone(user.getBusiness().getPhone());
            dto.setUrlLogo(user.getBusiness().getUrlLogo());

            User owner = user.getBusiness().getOwner();
            if (owner != null) {
                dto.setOwnerName(owner.getName());
                dto.setOwnerLastName(owner.getLastName());
                dto.setOwnerMail(owner.getMail());
            }

        }

        if (contract != null) {
            dto.setStartDate(contract.getStartDate());
            dto.setEndDate(contract.getEndDate());
            dto.setStatus(contract.getStatus());
            dto.setNextPaymentDate(contract.getNextPaymentDate());
        }

        if (payment != null) {
            dto.setPaymentMethod(payment.getPaymentMethod());
            dto.setKindOfPayment(payment.getKindOfPayment());
        }

        return dto;
    }

    @Transactional
    public void updateBusinessStatus(Long businessId, String newStatus) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Negocio no encontrado"));

        business.setStatus(newStatus.toUpperCase());
        businessRepository.save(business);
    }
}
