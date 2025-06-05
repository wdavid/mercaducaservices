package com.project.mercaduca.services;

import com.project.mercaduca.dtos.BusinessRequestCreateDTO;
import com.project.mercaduca.dtos.BusinessSummaryDTO;
import com.project.mercaduca.models.Business;
import com.project.mercaduca.models.BusinessRequest;
import com.project.mercaduca.models.User;
import com.project.mercaduca.repositories.BusinessRepository;
import com.project.mercaduca.repositories.BusinessRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.project.mercaduca.repositories.RoleRepository;
import com.project.mercaduca.repositories.UserRepository;
import com.project.mercaduca.services.EmailService;

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
                "<body style='font-family: Arial, sans-serif;'>" +
                "<h2>Bienvenido a Mercaduca 🎉</h2>" +
                "<p>Tu usuario es: <strong>" + user.getMail() + "</strong></p>" +
                "<p>Tu contraseña temporal es: <strong>" + tempPassword + "</strong></p>" +
                "<p>Por favor, cambia tu contraseña después de iniciar sesión.</p>" +
                "<br>" +
                "<p>Saludos,<br>El equipo de Mercaduca</p>" +
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
                "<body style='font-family: Arial, sans-serif;'>" +
                "<h2>Solicitud Rechazada 😢</h2>" +
                "<p>Lamentamos informarte que tu solicitud ha sido <strong>rechazada</strong>.</p>" +
                "<p>Motivo: <em>" + reason + "</em></p>" +
                "<br>" +
                "<p>Si tienes dudas, contáctanos.</p>" +
                "<p>Saludos,<br>El equipo de Mercaduca</p>" +
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
}
