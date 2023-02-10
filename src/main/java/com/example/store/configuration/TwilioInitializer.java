package com.example.store.configuration;

import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TwilioInitializer {
    private TwilioConfiguration twilioConfig;

    @Autowired
    public TwilioInitializer(TwilioConfiguration twilioConfig) {
        this.twilioConfig = twilioConfig;
        Twilio.init(
                twilioConfig.getSid(),
                twilioConfig.getToken()
        );
    }
}
