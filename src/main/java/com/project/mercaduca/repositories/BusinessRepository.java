package com.project.mercaduca.repositories;

import com.project.mercaduca.dtos.BusinessNameLogoDTO;
import com.project.mercaduca.models.Business;
import com.project.mercaduca.models.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

    @Query("SELECT b.businessName as businessName, b.urlLogo as urlLogo FROM Business b WHERE b.status = 'APROBADO'")
    List<BusinessNameLogoDTO> findApprovedBusinessNameAndLogo();

    @EntityGraph(attributePaths = {"products"})
    List<Business> findByStatus(String status);

    Optional<Business> findById(Long businessId);
    Optional<Business> findByOwner(User owner);

}