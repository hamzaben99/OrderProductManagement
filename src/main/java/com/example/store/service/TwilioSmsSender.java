package com.example.store.service;

import com.example.store.configuration.TwilioConfiguration;
import com.example.store.entity.Order;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class TwilioSmsSender implements SmsSender {
    private String bodyTemplate = "Bonjour {clientName},\n" +
                                " \n" +
                                "Votre Commande No {orderId} du {orderDate} vient d'être validée."; // todo: Add template

    @Autowired
    private TwilioConfiguration twilioConfig;

    @Async
    public void sendSms(Order order) {
        bodyTemplate = bodyTemplate.replace("{clientName}", order.getClient().getFirstName() + order.getClient().getLastName());
        bodyTemplate = bodyTemplate.replace("{orderId}", order.getId().toString());
        bodyTemplate = bodyTemplate.replace("{orderDate}", order.getOrderDate().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));

        String phoneNumber = order.getClient().getPhoneNumber();
        MessageCreator creator = Message.creator(
                new PhoneNumber(phoneNumber),
                new PhoneNumber(twilioConfig.getNumber()),
                bodyTemplate
        );
        creator.create();
    }
}
