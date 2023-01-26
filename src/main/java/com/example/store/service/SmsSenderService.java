package com.example.store.service;

import com.example.store.configuration.TwilioConfiguration;
import com.example.store.entity.Order;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Service
public class SmsSenderService {
    private String bodyTemplate = "test {clientName}, at {orderDate}"; // Add template

    @Autowired
    private TwilioConfiguration twilioConfig;

    public void sendSms(Order order) {
        bodyTemplate = bodyTemplate.replace("{clientName}", order.getClient().getLastName());
        bodyTemplate = bodyTemplate.replace("{orderDate}", order.getOrderDate().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));

        MessageCreator creator = Message.creator(
                new PhoneNumber(order.getClient().getPhoneNumber()),
                new PhoneNumber(twilioConfig.getNumber()),
                bodyTemplate
        );
        creator.create();
    }
}
