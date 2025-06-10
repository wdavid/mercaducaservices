package com.project.mercaduca.repositories;


import com.project.mercaduca.dtos.BusinessSummaryDTO;
import com.project.mercaduca.models.BusinessRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessRequestRepository extends JpaRepository<BusinessRequest, Long> {
    Optional<BusinessRequest> findByUserEmail(String userEmail);
    @Query("SELECT new com.project.mercaduca.dtos.BusinessSummaryDTO(b.id, b.businessName, b.urlLogo) FROM Business b WHERE b.status = 'ACTIVO'")
    List<BusinessSummaryDTO> findApprovedBusinessSummaries();
    boolean existsByUserEmailAndStatusIn(String userEmail, List<String> status);
}
