package com.project.mercaduca.services;

import com.project.mercaduca.dtos.BusinessWithProductsDTO;
import com.project.mercaduca.dtos.ProductCreateDTO;
import com.project.mercaduca.dtos.ProductPriceResponseDTO;
import com.project.mercaduca.dtos.ProductResponseDTO;
import com.project.mercaduca.exceptions.MaxProductsReachedException;
import com.project.mercaduca.models.*;
import com.project.mercaduca.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    public Double roundPrice(Double price) {
        if (price == null) return null;
        return BigDecimal.valueOf(price)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    @Autowired
    private AuthService authService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private ProductPriceRepository productPriceRepository;

    @Autowired
    private ProductApprovalRepository productApprovalRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    /*
    public void createProduct(ProductCreateDTO dto, Long businessId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business no encontrado"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setStock(dto.getStock());
        product.setUrlImage(dto.getUrlImage());
        product.setStatus("PENDIENTE");
        product.setBusiness(business);
        product.setCategory(category);
        product = productRepository.save(product);

        ProductPrice price = new ProductPrice();
        price.setPrice(roundPrice(dto.getPrice()));
        price.setStartDate(LocalDate.now());
        price.setProduct(product);
        productPriceRepository.save(price);

        ProductApproval approval = new ProductApproval();
        approval.setStatus("PENDIENTE");
        approval.setReviewDate(null);
        approval.setRemarks(null);
        approval.setProduct(product);
        productApprovalRepository.save(approval);
    }*/
    public void createProduct(ProductCreateDTO dto) {
        User user = authService.getAuthenticatedUser();

        Business business = businessRepository.findByOwner(user)
                .orElseThrow(() -> new RuntimeException("El usuario no tiene un negocio asociado"));

        long productCount = productRepository.countByBusiness(business);
        if (productCount >= 7) {
            throw new MaxProductsReachedException("No se pueden crear más de 7 productos por usuario");
        }

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setStock(dto.getStock());
        product.setUrlImage(dto.getUrlImage());
        product.setStatus("PENDIENTE");
        product.setBusiness(business);
        product.setCategory(category);
        product = productRepository.save(product);

        ProductPrice price = new ProductPrice();
        price.setPrice(roundPrice(dto.getPrice()));
        price.setStartDate(LocalDate.now());
        price.setProduct(product);
        productPriceRepository.save(price);

        ProductApproval approval = new ProductApproval();
        approval.setStatus("PENDIENTE");
        approval.setReviewDate(null);
        approval.setRemarks(null);
        approval.setProduct(product);
        productApprovalRepository.save(approval);
    }



    @Transactional
    public void reviewProduct(Long productId, boolean aprobado, String remarks) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        ProductApproval approval = productApprovalRepository.findByProduct(product)
                .orElseThrow(() -> new RuntimeException("Aprobación no encontrada"));

        approval.setStatus(aprobado ? "APROBADO" : "RECHAZADO");
        approval.setReviewDate(LocalDate.now());
        approval.setRemarks(remarks);
        productApprovalRepository.save(approval);

        product.setStatus(approval.getStatus());
        productRepository.save(product);

        User user = product.getBusiness().getOwner();

        String htmlMessage = "<html>" +
                "<body style='font-family: Arial, sans-serif;'>" +
                "<h2>Resultado de validación de producto</h2>" +
                "<p>Hola <strong>" + user.getName() + "</strong>,</p>" +
                "<p>Tu producto <strong>" + product.getName() + "</strong> ha sido <strong>" + approval.getStatus().toLowerCase() + "</strong>.</p>" +
                (remarks != null && !remarks.isBlank() ? "<p><strong>Observaciones:</strong> " + remarks + "</p>" : "") +
                "<br><p>Saludos,<br>Equipo de Mercaduca</p>" +
                "</body></html>";

        emailService.sendHtml(
                user.getMail(),
                "Resultado de validación de tu producto",
                htmlMessage
        );
    }

    public List<ProductResponseDTO> getApprovedProducts() {
        List<Product> products = productRepository.findByStatus("APROBADO");

        return products.stream()
                .map(product -> new ProductResponseDTO(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getStock(),
                        product.getStatus(),
                        product.getUrlImage(),
                        product.getBusiness().getOwner().getName(),
                        product.getCategory().getName(),
                        getCurrentPriceDTO(product.getId())
                ))
                .collect(Collectors.toList());
    }


    public List<ProductResponseDTO> getPendingProducts(Long businessId) {
        List<String> statuses = List.of("PENDIENTE", "RECHAZADO");
        List<Product> products = productRepository.findByStatusInAndBusinessId(statuses, businessId);

        return products.stream()
                .map(product -> new ProductResponseDTO(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getStock(),
                        product.getStatus(),
                        product.getUrlImage(),
                        product.getBusiness().getOwner().getName(),
                        product.getCategory().getName(),
                        getCurrentPriceDTO(product.getId())
                ))
                .collect(Collectors.toList());
    }


    /*public List<Product> getProductsByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return productRepository.findByUser(user);
    }*/

    public List<ProductResponseDTO> getProductsByBusiness(Long businessId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Negocio no encontrado"));

        return productRepository.findByBusiness(business).stream()
                .map(product -> new ProductResponseDTO(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getStock(),
                        product.getStatus(),
                        product.getUrlImage(),
                        product.getBusiness().getOwner().getName(),
                        product.getCategory().getName(),
                        getCurrentPriceDTO(product.getId())
                ))
                .collect(Collectors.toList());
    }

    public BusinessWithProductsDTO getBusinessWithApprovedProducts(Long businessId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Negocio no encontrado"));

        List<Product> approvedProducts = productRepository.findByBusinessAndStatus(business, "APROBADO");

        List<ProductResponseDTO> productDTOs = approvedProducts.stream()
                .map(product -> new ProductResponseDTO(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getStock(),
                        product.getStatus(),
                        product.getUrlImage(),
                        product.getBusiness().getOwner().getName(),
                        product.getCategory().getName(),
                        getCurrentPriceDTO(product.getId())
                ))
                .collect(Collectors.toList());

        return new BusinessWithProductsDTO(
                business.getId(),
                business.getBusinessName(),
                business.getDescription(),
                business.getSector(),
                business.getProductType(),
                business.getPriceRange(),
                business.getFacebook(),
                business.getInstagram(),
                business.getPhone(),
                business.getUrlLogo(),
                productDTOs
        );
    }

    private ProductPriceResponseDTO getCurrentPriceDTO(Long productId) {
        return productPriceRepository.findTopByProductIdAndEndDateIsNullOrderByStartDateDesc(productId)
                .map(price -> {
                    ProductPriceResponseDTO dto = new ProductPriceResponseDTO();
                    dto.setId(price.getId());
                    dto.setPrice(price.getPrice());
                    dto.setStartDate(price.getStartDate());
                    dto.setEndDate(price.getEndDate());
                    dto.setProductId(productId);
                    return dto;
                })
                .orElse(null);
    }



}
