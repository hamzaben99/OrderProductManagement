package com.example.store.service;

import com.example.store.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;
    private static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private String bodyTemplate =
                    "Bonjour {ClientName},\n" +
                    "\n" +
                    "Votre Commande No {orderId} du {orderDate} vient d'être validée";

    private String subjectTemplate = "Your order has been validated";

    @Async
    public void sendEmail(Order order) {
        SimpleMailMessage message = new SimpleMailMessage();

        bodyTemplate = bodyTemplate.replace("{ClientName}", order.getClient().getLastName());
        bodyTemplate = bodyTemplate.replace("{orderDate}", order.getOrderDate().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));
        bodyTemplate = bodyTemplate.replace("{orderDate}", order.getOrderDate().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));

        message.setTo(order.getClient().getEmailAddress());
        message.setSubject(subjectTemplate);
        message.setText(bodyTemplate);

        mailSender.send(message);
    }

    public void checkEmail(String email) {
        if (email != null && !email.matches(EMAIL_REGEX))
            throw new IllegalArgumentException("Invalid Email Address");
    }
}
