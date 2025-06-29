package com.project.mercaduca.services;

import com.project.mercaduca.dtos.BusinessApprovedDTO;
import com.project.mercaduca.dtos.ContractRequestDTO;
import com.project.mercaduca.models.*;
import com.project.mercaduca.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private MajorRepository majorRepository;

    @Transactional
    public void deleteAnyBusiness(Long businessId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Negocio no encontrado"));

        business.setStatus("ELIMINADO");
        businessRepository.save(business);
    }

    @Transactional
    public Page<BusinessApprovedDTO> getFilteredBusinesses(List<String> statuses, Boolean tienenContrato, Boolean tienenProductosPendientes, Pageable pageable) {
        Page<Business> businesses = businessRepository.findByStatusesContratoAndProductosPendientes(statuses, tienenContrato, tienenProductosPendientes, pageable);

        Set<Long> facultyIds = new HashSet<>();
        Set<Long> majorIds = new HashSet<>();

        for (Business b : businesses.getContent()) {
            User owner = b.getOwner();
            if (isNumeric(owner.getFaculty())) {
                facultyIds.add(Long.valueOf(owner.getFaculty()));
            }
            if (isNumeric(owner.getMajor())) {
                majorIds.add(Long.valueOf(owner.getMajor()));
            }
        }

        Map<Long, String> facultyNames = facultyRepository.findAllById(facultyIds).stream()
                .collect(Collectors.toMap(Faculty::getId, Faculty::getName));

        Map<Long, String> majorNames = majorRepository.findAllById(majorIds).stream()
                .collect(Collectors.toMap(Major::getId, Major::getName));

        return businesses.map(business -> {
            User owner = business.getOwner();

            String facultyValue = owner.getFaculty();
            String facultyName;
            if (facultyValue != null && isNumeric(facultyValue)) {
                facultyName = facultyNames.getOrDefault(Long.valueOf(facultyValue), "Desconocida");
            } else {
                facultyName = facultyValue != null ? facultyValue : "Desconocida";
            }

            String majorValue = owner.getMajor();
            String majorName;
            if (majorValue != null && isNumeric(majorValue)) {
                majorName = majorNames.getOrDefault(Long.valueOf(majorValue), "Desconocida");
            } else {
                majorName = majorValue != null ? majorValue : "Desconocida";
            }

            boolean hasPendingProducts = business.getProducts() != null &&
                    business.getProducts().stream().anyMatch(p -> "PENDIENTE".equalsIgnoreCase(p.getStatus()));

            return new BusinessApprovedDTO(
                    owner.getId(),
                    business.getUrlLogo(),
                    business.getBusinessName(),
                    owner.getName() + " " + owner.getLastName(),
                    owner.getMail(),
                    majorName,
                    facultyName,
                    hasPendingProducts
            );
        });
    }

    public boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
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
            dto.setStatusBusiness(user.getBusiness().getStatus());

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
    public void updateBusinessStatusByOwnerId(Long ownerId, String newStatus) {
        Business business = businessRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new RuntimeException("Negocio no encontrado para el usuario"));

        business.setStatus(newStatus.toUpperCase());
        businessRepository.save(business);
    }
}
