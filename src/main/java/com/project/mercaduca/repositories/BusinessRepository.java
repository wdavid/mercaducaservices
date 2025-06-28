package com.project.mercaduca.repositories;

import com.project.mercaduca.dtos.BusinessNameLogoDTO;
import com.project.mercaduca.models.Business;
import com.project.mercaduca.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @EntityGraph(attributePaths = {"products"})
    @Query("""
    SELECT b FROM Business b
    WHERE (:statuses IS NULL OR b.status IN :statuses)
    AND (
        (:tienenContrato = true AND EXISTS (
            SELECT c FROM Contract c WHERE c.user = b.owner AND c.status = 'ACTIVO'
        )) OR
        (:tienenContrato = false AND NOT EXISTS (
            SELECT c FROM Contract c WHERE c.user = b.owner AND c.status = 'ACTIVO'
        )) OR
        (:tienenContrato IS NULL)
    )
    AND (
        (:tienenProductosPendientes = true AND EXISTS (
            SELECT p FROM Product p WHERE p.business = b AND p.status = 'PENDIENTE'
        )) OR
        (:tienenProductosPendientes = false AND NOT EXISTS (
            SELECT p FROM Product p WHERE p.business = b AND p.status = 'PENDIENTE'
        )) OR
        (:tienenProductosPendientes IS NULL)
    )
""")
    Page<Business> findByStatusesContratoAndProductosPendientes(
            @Param("statuses") List<String> statuses,
            @Param("tienenContrato") Boolean tienenContrato,
            @Param("tienenProductosPendientes") Boolean tienenProductosPendientes,
            Pageable pageable
    );


    Optional<Business> findByOwnerId(Long ownerId);
}
