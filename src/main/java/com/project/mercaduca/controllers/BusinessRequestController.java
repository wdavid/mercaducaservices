package com.project.mercaduca.controllers;

import com.project.mercaduca.dtos.*;
import com.project.mercaduca.enums.EntrepeneurKind;
import com.project.mercaduca.enums.Gender;
import com.project.mercaduca.models.BusinessRequest;
import com.project.mercaduca.services.BusinessRequestService;
import com.project.mercaduca.services.CloudinaryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/business-requests")
public class BusinessRequestController {

    @Autowired
    private CloudinaryService cloudinaryService;

    private final BusinessRequestService businessRequestService;

    public BusinessRequestController(CloudinaryService cloudinaryService, BusinessRequestService businessRequestService) {
        this.cloudinaryService = cloudinaryService;
        this.businessRequestService = businessRequestService;
    }

    @GetMapping("/gender")
    public ResponseEntity<Gender[]> getGender() {
        return ResponseEntity.ok(Gender.values());
    }

    @GetMapping("/entrepeneurkind")
    public ResponseEntity<EntrepeneurKind[]> getEntrepreneurKind() {
        return ResponseEntity.ok(EntrepeneurKind.values());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createRequest(
            @Valid @ModelAttribute BusinessRequestCreateDTO dto,
            @RequestParam(value = "logo", required = false) MultipartFile logo
    ) {
        try {
            String logoUrl = (logo != null && !logo.isEmpty()) ? cloudinaryService.uploadImage(logo) : null;
            dto.setUrlLogo(logoUrl);

            businessRequestService.createBusinessRequest(dto);

            return ResponseEntity.ok(Map.of("message", "Solicitud enviada correctamente."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Ocurrió un error inesperado al crear la solicitud."));
        }
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BusinessRequestResponseDTO>> getAllRequests() {
        List<BusinessRequest> requests = businessRequestService.getAllBusinessRequests();

        List<BusinessRequestResponseDTO> responseDTOs = requests.stream().map(req -> {
            BusinessRequestResponseDTO dto = new BusinessRequestResponseDTO();
            dto.setId(req.getId());
            dto.setUrlLogo(req.getUrlLogo());
            dto.setBusinessName(req.getBusinessName());
            dto.setDescription(req.getDescription());
            dto.setStatus(req.getStatus());
            dto.setSubmissionDate(req.getSubmissionDate());
            dto.setSector(req.getSector());
            dto.setProductType(req.getProductType());
            dto.setPriceRange(req.getPriceRange());
            dto.setFacebook(req.getFacebook());
            dto.setInstagram(req.getInstagram());
            dto.setPhone(req.getPhone());
            dto.setUserName(req.getUserName());
            dto.setUserLastName(req.getUserLastName());
            dto.setUserEmail(req.getUserEmail());
            dto.setEntrepeneurKind(req.getEntrepeneurKind());
            dto.setUserGender(req.getUserGender());
            return dto;
        }).collect(Collectors.toList());

        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> approveRequest(@PathVariable Long id) {
        businessRequestService.approveRequest(id);
        return new ResponseEntity<>("Solicitud aprobada y usuario creado exitosamente.", HttpStatus.OK);
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> rejectRequest(@PathVariable Long id, @RequestBody RejectRequestDTO dto) {
        businessRequestService.rejectRequest(id, dto.getReason());
        return new ResponseEntity<>("Solicitud rechazada.", HttpStatus.OK);
    }



    @GetMapping("/approved-summary")
    public Page<BusinessSummaryDTO> getApprovedBusinessSummaries(
            @RequestParam(value = "search", required = false) String searchTerm,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return businessRequestService.getApprovedBusinessSummaries(searchTerm, pageable);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteRequest(@PathVariable Long id) {
        businessRequestService.deleteRequest(id);
    }

    @GetMapping("/filter")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<BusinessContractRequestDTO>> getBusinessRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "desc") String sortDirection
    ) {
        Page<BusinessContractRequestDTO> requests = businessRequestService.getBusinessRequestsByStatusAndOrder(page, size, status, sortDirection);
        return ResponseEntity.ok(requests);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BusinessContractRequestDTO> getRequestById(@PathVariable Long id) {
        BusinessRequest request = businessRequestService.getRequestById(id);

        BusinessContractRequestDTO dto = new BusinessContractRequestDTO();
        dto.setId(request.getId());
        dto.setUrlLogo(request.getUrlLogo());
        dto.setBusinessName(request.getBusinessName());
        dto.setDescription(request.getDescription());
        dto.setStatus(request.getStatus());
        dto.setSubmissionDate(request.getSubmissionDate());
        dto.setSector(request.getSector());
        dto.setProductType(request.getProductType());
        dto.setPriceRange(request.getPriceRange());
        dto.setFacebook(request.getFacebook());
        dto.setInstagram(request.getInstagram());
        dto.setPhone(request.getPhone());
        dto.setUserName(request.getUserName());
        dto.setUserLastName(request.getUserLastName());
        dto.setUserEmail(request.getUserEmail());
        dto.setEntrepeneurKind(request.getEntrepeneurKind());
        dto.setUserGender(request.getUserGender());
        dto.setUserBirthDate(request.getUserBirthDate());

        // Faculty
        String facultyValue = request.getUserFaculty();
        if (facultyValue != null) {
            if (businessRequestService.isNumeric(facultyValue)) {
                Long facultyId = Long.valueOf(facultyValue);
                String facultyName = businessRequestService.getFacultyRepository()
                        .findById(facultyId)
                        .map(f -> f.getName())
                        .orElse("Desconocida");
                dto.setFacultyName(facultyName);
            } else {
                dto.setFacultyName(facultyValue);
            }
        }

        // Major
        String majorValue = request.getUserMajor();
        if (majorValue != null) {
            if (businessRequestService.isNumeric(majorValue)) {
                Long majorId = Long.valueOf(majorValue);
                String majorName = businessRequestService.getMajorRepository()
                        .findById(majorId)
                        .map(m -> m.getName())
                        .orElse("Desconocida");
                dto.setMajorName(majorName);
            } else {
                dto.setMajorName(majorValue);
            }
        }

        return ResponseEntity.ok(dto);
    }


}
