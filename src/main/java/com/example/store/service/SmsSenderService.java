package com.example.store.service;

import com.example.store.configuration.TwilioConfiguration;
import com.example.store.entity.Order;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Service
public class SmsSenderService {
    private static final String PHONE_NUMBER_REGEX = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$";

    private String bodyTemplate = "Bonjour {ClientName},\n" +
                                " \n" +
                                "Votre Commande No {orderId} du {orderDate} vient d'être validée"; // Add template

    @Autowired
    private TwilioConfiguration twilioConfig;

    @Async
    public void sendSms(Order order) {
        if (twilioConfig.isEnabled()) {
            bodyTemplate = bodyTemplate.replace("{clientName}", order.getClient().getLastName());
            bodyTemplate = bodyTemplate.replace("{orderId}", order.getClient().getId().toString());
            bodyTemplate = bodyTemplate.replace("{orderDate}", order.getOrderDate().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));

            String phoneNumber;
            if (order.getClient().getPhoneNumber().startsWith(twilioConfig.getCountryCode()))
                phoneNumber = order.getClient().getPhoneNumber();
            else if (order.getClient().getPhoneNumber().startsWith("0"))
                phoneNumber = order.getClient().getPhoneNumber().replace("0", twilioConfig.getCountryCode());
            else
                throw new IllegalArgumentException("Unsupported country code");

            MessageCreator creator = Message.creator(
                    new PhoneNumber(phoneNumber),
                    new PhoneNumber(twilioConfig.getNumber()),
                    bodyTemplate
            );
            creator.create();
        }
    }

    public String formatPhoneNumber(String phoneNumber) {
        if (!phoneNumber.replace(" ", "").matches(PHONE_NUMBER_REGEX))
            throw new IllegalArgumentException("Invalid Phone Number");

        String validPhoneNumber;
        if (phoneNumber.startsWith(twilioConfig.getCountryCode()))
            validPhoneNumber = phoneNumber;
        else if (phoneNumber.startsWith("0"))
            validPhoneNumber = phoneNumber.replace("0", twilioConfig.getCountryCode());
        else
            throw new IllegalArgumentException("Unsupported country code");
        return validPhoneNumber;
    }
}
