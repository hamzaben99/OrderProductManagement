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

    private String bodyTemplate =
                    "Bonjour {ClientName},\n" +
                    "\n" +
                    "Votre Commande du {orderDate} vient d'être validée par le magazin";

    private String subjectTemplate = "Your order has been validated";

    @Async
    public void sendEmail(Order order) {
        SimpleMailMessage message = new SimpleMailMessage();

        bodyTemplate = bodyTemplate.replace("{ClientName}", order.getClient().getLastName());
        bodyTemplate = bodyTemplate.replace("{orderDate}", order.getOrderDate().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));

        message.setTo(order.getClient().getEmailAddress());
        message.setSubject(subjectTemplate);
        message.setText(bodyTemplate);

        mailSender.send(message);
    }
}
