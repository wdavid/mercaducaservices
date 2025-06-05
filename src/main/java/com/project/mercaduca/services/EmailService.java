package com.project.mercaduca.services;

import com.project.mercaduca.models.Product;
import com.project.mercaduca.models.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void send(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("wmmartinezhdz@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendHtml(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendProductApprovalSummaryEmail(User user, List<Product> approved, List<Product> rejected, String remark) {
        String subject = "Resumen de revisión de productos";
        StringBuilder htmlBody = new StringBuilder();

        htmlBody.append("<div style='font-family: Arial, sans-serif;'>");
        htmlBody.append("<h2>Hola ").append(user.getName()).append(",</h2>");
        htmlBody.append("<p>Este es el resumen de la revisión de tus productos:</p>");

        if (!approved.isEmpty()) {
            htmlBody.append("<h3 style='color:green;'>✅ Productos Aprobados:</h3>");
            htmlBody.append("<table style='border-collapse: collapse; width: 100%; margin-bottom: 20px;'>");
            htmlBody.append("<thead><tr style='background-color: #e6ffed;'>")
                    .append("<th style='border: 1px solid #ccc; padding: 8px;'>Imagen</th>")
                    .append("<th style='border: 1px solid #ccc; padding: 8px;'>Nombre</th>")
                    .append("<th style='border: 1px solid #ccc; padding: 8px;'>Descripción</th>")
                    .append("<th style='border: 1px solid #ccc; padding: 8px;'>Stock</th>")
                    .append("<th style='border: 1px solid #ccc; padding: 8px;'>Categoría</th>")
                    .append("</tr></thead><tbody>");
            for (Product product : approved) {
                htmlBody.append("<tr>")
                        .append("<td style='border: 1px solid #ccc; padding: 8px; text-align: center;'><img src='")
                        .append(product.getUrlImage())
                        .append("' alt='imagen' width='50' height='50'/></td>")
                        .append("<td style='border: 1px solid #ccc; padding: 8px;'>").append(product.getName()).append("</td>")
                        .append("<td style='border: 1px solid #ccc; padding: 8px;'>").append(product.getDescription()).append("</td>")
                        .append("<td style='border: 1px solid #ccc; padding: 8px;'>").append(product.getStock()).append("</td>")
                        .append("<td style='border: 1px solid #ccc; padding: 8px;'>").append(product.getCategory().getName()).append("</td>")
                        .append("</tr>");
            }
            htmlBody.append("</tbody></table>");
        }

        if (!rejected.isEmpty()) {
            htmlBody.append("<h3 style='color:red;'>❌ Productos Rechazados:</h3>");
            htmlBody.append("<table style='border-collapse: collapse; width: 100%; margin-bottom: 20px;'>");
            htmlBody.append("<thead><tr style='background-color: #ffe6e6;'>")
                    .append("<th style='border: 1px solid #ccc; padding: 8px;'>Imagen</th>")
                    .append("<th style='border: 1px solid #ccc; padding: 8px;'>Nombre</th>")
                    .append("<th style='border: 1px solid #ccc; padding: 8px;'>Descripción</th>")
                    .append("<th style='border: 1px solid #ccc; padding: 8px;'>Stock</th>")
                    .append("<th style='border: 1px solid #ccc; padding: 8px;'>Categoría</th>")
                    .append("</tr></thead><tbody>");
            for (Product product : rejected) {
                htmlBody.append("<tr>")
                        .append("<td style='border: 1px solid #ccc; padding: 8px; text-align: center;'><img src='")
                        .append(product.getUrlImage())
                        .append("' alt='imagen' width='50' height='50'/></td>")
                        .append("<td style='border: 1px solid #ccc; padding: 8px;'>").append(product.getName()).append("</td>")
                        .append("<td style='border: 1px solid #ccc; padding: 8px;'>").append(product.getDescription()).append("</td>")
                        .append("<td style='border: 1px solid #ccc; padding: 8px;'>").append(product.getStock()).append("</td>")
                        .append("<td style='border: 1px solid #ccc; padding: 8px;'>").append(product.getCategory().getName()).append("</td>")
                        .append("</tr>");
            }
            htmlBody.append("</tbody></table>");
        }

        htmlBody.append("<p><strong>Observaciones generales:</strong><br>").append(remark).append("</p>");
        htmlBody.append("<p>Gracias por usar <strong>Mercaduca 🚀</strong></p>");
        htmlBody.append("</div>");

        sendHtml(user.getMail(), subject, htmlBody.toString());
    }


}
