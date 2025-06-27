package com.project.mercaduca.services;

import com.project.mercaduca.dtos.BusinessRequestCreateDTO;
import com.project.mercaduca.dtos.BusinessSummaryDTO;
import com.project.mercaduca.models.Business;
import com.project.mercaduca.models.BusinessRequest;
import com.project.mercaduca.models.User;
import com.project.mercaduca.repositories.BusinessRepository;
import com.project.mercaduca.repositories.BusinessRequestRepository;
import com.project.mercaduca.repositories.RoleRepository;
import com.project.mercaduca.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class BusinessRequestService {
    private final BusinessRequestRepository businessRequestRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final BusinessRepository businessRepository;

    // Constructor actualizado
    public BusinessRequestService(
            BusinessRequestRepository businessRequestRepository,
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService,
            BusinessRepository businessRepository
    ) {
        this.businessRequestRepository = businessRequestRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.businessRepository = businessRepository;
    }

    public BusinessRequest createBusinessRequest(BusinessRequestCreateDTO dto) {
        if (userRepository.existsByMail(dto.getUserEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario registrado con este correo.");
        }

        boolean emailYaSolicitado = businessRequestRepository.existsByUserEmailAndStatusIn(
                dto.getUserEmail(),
                List.of("PENDIENTE", "APROBADO")
        );

        if (emailYaSolicitado) {
            throw new IllegalArgumentException("Ya existe una solicitud activa con este correo.");
        }

        BusinessRequest request = new BusinessRequest();

        request.setUrlLogo(dto.getUrlLogo());
        request.setBusinessName(dto.getBusinessName());
        request.setDescription(dto.getDescription());
        request.setSector(dto.getSector());
        request.setProductType(dto.getProductType());
        request.setPriceRange(dto.getPriceRange());
        request.setFacebook(dto.getFacebook());
        request.setInstagram(dto.getInstagram());
        request.setPhone(dto.getPhone());

        request.setUserName(dto.getUserName());
        request.setUserLastName(dto.getUserLastName());
        request.setUserEmail(dto.getUserEmail());
        request.setUserGender(dto.getUserGender());
        request.setUserBirthDate(dto.getUserBirthDate());
        request.setUserFaculty(dto.getUserFaculty());
        request.setUserMajor(dto.getUserMajor());
        request.setEntrepeneurKind(dto.getEntrepeneurKind());

        request.setStatus("PENDIENTE");
        request.setSubmissionDate(LocalDate.now());

        return businessRequestRepository.save(request);
    }


    public List<BusinessRequest> getAllBusinessRequests() {
        return businessRequestRepository.findAll();
    }

    public void approveRequest(Long requestId) {
        BusinessRequest request = businessRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        System.out.println("Solicitud encontrada: " + request.getId());

        if (!"PENDIENTE".equalsIgnoreCase(request.getStatus())) {
            throw new IllegalStateException("Esta solicitud ya fue procesada");
        }

        // Crear usuario
        String tempPassword = UUID.randomUUID().toString().substring(0, 8);
        String encodedPassword = passwordEncoder.encode(tempPassword);

        User user = new User();
        user.setName(request.getUserName());
        user.setLastName(request.getUserLastName());
        user.setMail(request.getUserEmail());
        user.setGender(request.getUserGender());
        user.setBirthDate(request.getUserBirthDate());
        user.setFaculty(request.getUserFaculty());
        user.setMajor(request.getUserMajor());
        user.setPassword(encodedPassword);
        user.setEntrepeneurKind(request.getEntrepeneurKind());
        user.setRole(roleRepository.findByName("ROLE_EMPRENDEDOR").orElseThrow());

        userRepository.save(user);

        // Crear negocio
        Business business = new Business();
        business.setBusinessName(request.getBusinessName());
        business.setDescription(request.getDescription());
        business.setStatus("ACTIVO");
        business.setSubmissionDate(request.getSubmissionDate());
        business.setReviewDate(LocalDate.now());
        business.setSector(request.getSector());
        business.setProductType(request.getProductType());
        business.setPriceRange(request.getPriceRange());
        business.setFacebook(request.getFacebook());
        business.setInstagram(request.getInstagram());
        business.setPhone(request.getPhone());
        business.setUrlLogo(request.getUrlLogo());
        business.setOwner(user);

        businessRepository.save(business);

        // Actualizar estado de solicitud
        request.setStatus("APROBADO");
        request.setReviewDate(LocalDate.now());
        businessRequestRepository.save(request);

        // Enviar correo
        String htmlMessage = "<html>" +
                "<head>" +
                "<style>" +
                "body {font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 0px;}" +
                ".card {background-color: #ffffff; border-radius: 10px; padding: 5px; max-width: 600px; margin: auto; box-shadow: 0 4px 10px rgba(0,0,0,0.1);}" +
                ".header {background-color: rgb(99, 107, 101); padding: 10px; text-align: center; border-radius: 10px 10px 0 0;}" +
                ".header h2, .footer p {color: white; margin: 0;}" +
                ".content {padding: 20px;}" +
                "table {width: 100%; border-collapse: collapse; margin: 15px 0;}" +
                "table td {padding: 10px; border: 1px solid #ddd; font-size: 16px;}" +
                "table td.label {background-color: #f0f0f0; font-weight: bold; width: 40%;}" +
                ".footer {background-color: rgb(99, 107, 101); padding: 10px; text-align: center; border-radius: 0 0 10px 10px;}" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='card'>" +
                "<div class='header'>" +
                "<h2>Bienvenido a Mercaduca 🎉</h2>" +
                "</div>" +
                "<div class='content'>" +
                "<p>¡Nos alegra darte la bienvenida! Aquí están tus credenciales:</p>" +
                "<table>" +
                "<tr><td class='label'>Usuario</td><td>" + user.getMail() + "</td></tr>" +
                "<tr><td class='label'>Contraseña</td><td>" + tempPassword + "</td></tr>" +
                "</table>" +
                "<p>Por favor, te invitamos a nuestras oficinas para la creación de tu contrato.</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>Saludos,<br>El equipo de Mercaduca</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";

        emailService.sendHtml(
                user.getMail(),
                "Tu cuenta en Mercaduca fue aprobada 🎉",
                htmlMessage
        );
    }

    public void rejectRequest(Long requestId, String reason) {
        BusinessRequest request = businessRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (!"PENDIENTE".equalsIgnoreCase(request.getStatus())) {
            throw new IllegalStateException("Esta solicitud ya fue procesada");
        }

        request.setStatus("RECHAZADO");
        request.setReviewDate(LocalDate.now());

        businessRequestRepository.save(request);

        String htmlRejection = "<html>" +
                "<head>" +
                "<style>" +
                "body {font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 0px;}" +
                ".card {background-color: #ffffff; border-radius: 10px; padding: 5px; max-width: 600px; margin: auto; box-shadow: 0 4px 10px rgba(0,0,0,0.1);}" +
                ".header {background-color: #b02a37; padding: 10px; text-align: center; border-radius: 10px 10px 0 0;}" +
                ".header h2, .footer p {color: white; margin: 0;}" +
                ".content {padding: 20px;}" +
                ".reason-box {background-color: #ffe5e5; border-left: 5px solid #b02a37; padding: 10px 15px; margin: 15px 0; font-style: italic; color: #900;}" +
                ".footer {background-color: #b02a37; padding: 10px; text-align: center; border-radius: 0 0 10px 10px;}" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='card'>" +
                "<div class='header'>" +
                "<h2>Solicitud Rechazada 😢</h2>" +
                "</div>" +
                "<div class='content'>" +
                "<p>Lamentamos informarte que tu solicitud ha sido <strong>rechazada</strong>.</p>" +
                "<div class='reason-box'>Motivo: " + reason + "</div>" +
                "<p>Si tienes dudas o deseas más información, no dudes en contactarnos.</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>Saludos,<br>El equipo de Mercaduca</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";


        emailService.sendHtml(
                request.getUserEmail(),
                "Tu solicitud a Mercaduca fue rechazada 😢",
                htmlRejection
        );
    }

    public List<BusinessSummaryDTO> getApprovedBusinessSummaries() {
        return businessRequestRepository.findApprovedBusinessSummaries();
    }

    public void deleteRequest(Long requestId) {
        businessRequestRepository.deleteById(requestId);
    }

    public Page<BusinessRequest> getBusinessRequestsByStatusAndOrder(
            int page,
            int size,
            String status,
            String sortDirection
    ) {
        Sort sort = Sort.by("submissionDate");
        sort = "desc".equalsIgnoreCase(sortDirection) ? sort.descending() : sort.ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        if (status == null || status.isBlank()) {
            return businessRequestRepository.findAll(pageable);
        }

        return businessRequestRepository.findByStatusContainingIgnoreCase(status, pageable);
    }

    public BusinessRequest getRequestById(Long id) {
        return businessRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
    }

}
