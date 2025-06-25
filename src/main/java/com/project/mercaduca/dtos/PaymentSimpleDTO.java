package com.project.mercaduca.dtos;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PaymentSimpleDTO {
    private Long id;
    private String paymentMethod;
    private String status;
    private LocalDate expectedDate;

}
